package maticzpl;

import maticzpl.constraints.AreaConstraint;
import maticzpl.constraints.BlockConstraint;
import maticzpl.constraints.PlaceConstraint;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Builder {

    public static AreaConstraint MiningAreaConstraint = new AreaConstraint();
    public static BlockConstraint MiningBlocksConstraint = new BlockConstraint();
    public static PlaceConstraint BlockPlacingConstraint = new PlaceConstraint();

    public KeyBinding toggleMining;
    public boolean isActive = false;

    public Builder() {
        this.toggleMining = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.maticzpl.toggle_active",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "category.maticzpl.liteedit"
        ));
    }

    public void StopMining() {
        if (isActive)
            ToggleMining();
    }

    public void StartMining() {
        if (!isActive)
            ToggleMining();
    }

    public void ToggleMining() {
        isActive = !isActive;

        MutableText message = Text.translatable(isActive ? "text.maticzpl.liteedit.on" : "text.maticzpl.liteedit.off");
        message.formatted(Formatting.GREEN);

        MinecraftClient.getInstance().inGameHud.setOverlayMessage(message, false);
    }

    public void Mine(MinecraftClient client) {
        if (toggleMining.wasPressed()) {
            ToggleMining();
        }

        if (isActive) {
            if (client.player == null) {
                isActive = false;
                return;
            }
            assert client.world != null;
            assert client.interactionManager != null;

            MiningBlocksConstraint.DoCache();
            MiningAreaConstraint.DoCache();

            var pos = client.player.getBlockPos();
            int r = 6;
            for (int x = -r; x < r; x++) {
                for (int y = -r; y < r; y++) {
                    for (int z = -r; z < r; z++) {
                        BlockPos block = pos.add(x, y, z);

                        if (!MiningAreaConstraint.Allowed(block))
                            continue;

                        if (!BlockPlacingConstraint.Allowed(block))
                            continue;

                        var dist = block.toCenterPos().distanceTo(client.player.getEyePos());
                        if (dist <= 5.0) {
                            if (MiningBlocksConstraint.Allowed(block)) {
                                for (Direction dir : Direction.values()) {
                                    Vec3d hit = block.toCenterPos().add(Vec3d.of(dir.getVector()).multiply(0.5));

                                    if (client.player.getEyePos().distanceTo(hit) > dist)
                                        continue;

                                    client.interactionManager.updateBlockBreakingProgress(block, dir);
                                    break;
                                }
                                client.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                            }
                        }

                    }
                }
            }
        }
    }

    public static void EquipItem(ItemStack stack, int slot) {
        var client = MinecraftClient.getInstance();
        client.interactionManager.clickCreativeStack(stack, 36 + slot);
    }

    public static void PlaceBlock(Block type, BlockPos pos) {
        var client = MinecraftClient.getInstance();

        assert client.player != null;
        assert client.interactionManager != null;

        var dist = pos.toCenterPos().distanceTo(client.player.getEyePos());
        if (dist > 5.0)
            return;

        // Get block item
        var slot = client.player.getInventory().selectedSlot;
        if (
                !client.player.getInventory().getMainHandStack()
                        .itemMatches(type.asItem().getDefaultStack()
                                .getRegistryEntry())
        ) {
            EquipItem(type.asItem().getDefaultStack(), slot);
        }

        // Place block
        Direction chosen = Direction.DOWN;
        for (Direction dir : Direction.values()) {
            Vec3d hit = pos.toCenterPos().add(Vec3d.of(dir.getVector()).multiply(0.5));

            if (client.player.getEyePos().distanceTo(hit) > dist)
                continue;

            chosen = dir;
            break;
        }

        var target = new BlockHitResult(
                pos.toCenterPos(),
                chosen,
                pos,
                false
        );

        client.interactionManager.interactBlock(
                client.player,
                client.player.getActiveHand(),
                target
        );


    }

    @Override
    public String toString() {
        return "§6[LiteEdit]§e " + (isActive ? "LiteEdit activated" : "LiteEdit disabled") + "\n " +
            MiningBlocksConstraint.toString()  + "\n " +
            MiningAreaConstraint.toString();
    }
}
