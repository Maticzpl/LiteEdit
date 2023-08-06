package maticzpl;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class Miner {
    public static Pair<BlockPos,BlockPos> areaLimit = null;

    public KeyBinding toggleMining;
    public boolean isMining = false;

    public Miner() {
        this.toggleMining = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.maticzpl.automine",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.maticzpl.automine"
        ));
    }

    public void Mine(MinecraftClient client) {
        if (toggleMining.wasPressed()) {
            isMining = !isMining;

            MutableText message = Text.translatable(isMining ? "text.maticzpl.automine.on" : "text.maticzpl.automine.off");
            message.formatted(Formatting.GREEN);

            client.inGameHud.setOverlayMessage(message, false);
        }

        if (isMining) {
            assert client.player != null;
            assert client.world != null;
            assert client.interactionManager != null;

            //var selected = client.player.getInventory().getMainHandStack().getRegistryEntry().value();
            var hotbar = new ArrayList<Item>();
            for (int i = 0; i <= 17; i++) {
                hotbar.add(client.player.getInventory().getStack(i).getRegistryEntry().value());
            }

            var pos = client.player.getBlockPos();
            int r = 6;
            for (int x = -r; x < r; x++) {
                for (int y = -r; y < r; y++) {
                    for (int z = -r; z < r; z++) {
                        BlockPos block = pos.add(x, y, z);

                        if (areaLimit != null) {
                            var minPos = new BlockPos(new Vec3i(
                                    Integer.min(areaLimit.getLeft().getX(), areaLimit.getRight().getX()),
                                    Integer.min(areaLimit.getLeft().getY(), areaLimit.getRight().getY()),
                                    Integer.min(areaLimit.getLeft().getZ(), areaLimit.getRight().getZ())
                            ));
                            var maxPos = new BlockPos(new Vec3i(
                                    Integer.max(areaLimit.getLeft().getX(), areaLimit.getRight().getX()),
                                    Integer.max(areaLimit.getLeft().getY(), areaLimit.getRight().getY()),
                                    Integer.max(areaLimit.getLeft().getZ(), areaLimit.getRight().getZ())
                            ));

                            if (
                                block.getX() >= maxPos.getX() || block.getX() <= minPos.getX() ||
                                block.getY() >= maxPos.getY() || block.getY() <= minPos.getY() ||
                                block.getZ() >= maxPos.getZ() || block.getZ() <= minPos.getZ()
                            )
                                continue;
                        }

                        boolean success = false;
                        var dist = block.toCenterPos().distanceTo(client.player.getEyePos());
                        if (dist <= 5.0) {
                            if (hotbar.contains(client.world.getBlockState(block).getBlock().asItem())) {
                                for (Direction dir : Direction.values()) {
                                    Vec3d hit = block.toCenterPos().add(Vec3d.of(dir.getVector()).multiply(0.5));

                                    if (client.player.getEyePos().distanceTo(hit) > dist)
                                        continue;

                                    if (client.interactionManager.updateBlockBreakingProgress(block, dir)) {
                                        success = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if (success)
                            client.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                    }
                }
            }
        }
    }
}
