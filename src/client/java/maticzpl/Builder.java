package maticzpl;

import maticzpl.jobs.JobArea;
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

import java.util.ArrayList;

public class Builder {
    static public SelectionArea selection = new SelectionArea();
    static public ArrayList<JobArea> jobs = new ArrayList<>();

    public KeyBinding toggleActive;
    public boolean isActive = false;

    public Builder() {
        this.toggleActive = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.maticzpl.liteedit.toggle_active",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "category.maticzpl.liteedit"
        ));
    }

    public void Deactivate() {
        if (isActive)
            ToggleActive();
    }

    public void Activate() {
        if (!isActive)
            ToggleActive();
    }

    public void ToggleActive() {
        isActive = !isActive;

        MutableText message = Text.translatable(isActive ? "text.maticzpl.liteedit.on" : "text.maticzpl.liteedit.off");
        message.formatted(Formatting.GREEN);

        MinecraftClient.getInstance().inGameHud.setOverlayMessage(message, false);
    }

    public void Build(MinecraftClient client) {
        if (toggleActive.wasPressed()) {
            ToggleActive();
        }

        if (isActive) {
            if (client.player == null) {
                isActive = false;
                return;
            }
            assert client.world != null;
            assert client.interactionManager != null;

            var pos = client.player.getBlockPos();
            int r = 6;
            for (int x = -r; x < r; x++) {
                for (int y = -r; y < r; y++) {
                    for (int z = -r; z < r; z++) {
                        BlockPos block = pos.add(x, y, z);

                        var dist = block.toCenterPos().distanceTo(client.player.getEyePos());
                        if (dist <= 5.0) {
                            for (int i = jobs.size()-1; i >= 0; i--) {
                                if (jobs.get(i).Do(block, dist))
                                    break;
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
        if (
            !client.player.getInventory().getMainHandStack()
                .itemMatches(type.asItem().getDefaultStack()
                .getRegistryEntry())
        ) {
            boolean found = false;
            for (int i = 0; i < 10; i++) {
                if(client.player.getInventory().getStack(i).itemMatches(type.asItem().getDefaultStack()
                        .getRegistryEntry())) {
                    client.player.getInventory().selectedSlot = i;
                    found = true;
                    break;
                }
            }

            if (!found) {
                for (int i = 0; i < 10; i++) {
                    if(client.player.getInventory().getStack(i).isEmpty()) {
                        client.player.getInventory().selectedSlot = i;
                        break;
                    }
                }
                EquipItem(type.asItem().getDefaultStack(), client.player.getInventory().selectedSlot);
            }

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

    public static void BreakBlock(BlockPos pos, double dist) {
        var client = MinecraftClient.getInstance();

        for (Direction dir : Direction.values()) {
            Vec3d hit = pos.toCenterPos().add(Vec3d.of(dir.getVector()).multiply(0.5));

            if (client.player.getEyePos().distanceTo(hit) > dist)
                continue;

            client.interactionManager.updateBlockBreakingProgress(pos, dir);
            break;
        }
        client.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }

    @Override
    public String toString() {
        return "§6[LiteEdit]§e " + (isActive ? "LiteEdit activated" : "LiteEdit disabled") + "\n ";// +
//            MiningBlocksConstraint.toString()  + "\n " +
//            MiningAreaConstraint.toString();
    }
}
