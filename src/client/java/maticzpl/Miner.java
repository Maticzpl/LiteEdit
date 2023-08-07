package maticzpl;

import maticzpl.constraints.AreaConstraint;
import maticzpl.constraints.BlockConstraint;
import maticzpl.constraints.Constraint;
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

    public static AreaConstraint MiningAreaConstraint = new AreaConstraint();
    public static BlockConstraint MiningBlocksConstraint = new BlockConstraint();

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
            if (client.player == null) {
                isMining = false;
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

                        boolean success = false;
                        var dist = block.toCenterPos().distanceTo(client.player.getEyePos());
                        if (dist <= 5.0) {
                            if (MiningBlocksConstraint.Allowed(block)) {
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

    @Override
    public String toString() {
        return "§6[AutoMiner]§e " + (isMining ? "Currently mining" : "Currently not mining") + "\n " +
                MiningBlocksConstraint.toString()  + "\n " +
                MiningAreaConstraint.toString();
    }
}
