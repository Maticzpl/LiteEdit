package maticzpl;

import maticzpl.utils.QuickChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class Wand {
    public Item WandItem;
    public int cooldown = 0;
    protected boolean firstCorner = true;
    protected BlockPos firstPos = null;

    public void DoWand() {
        var client = MinecraftClient.getInstance();
        if (client.crosshairTarget != null &&
            client.crosshairTarget.getType().equals(HitResult.Type.BLOCK) &&
            client.player.getInventory().getMainHandStack().getRegistryEntry().value().equals(WandItem)
        ) {
            // Prevent calling before releasing RMB
            if (cooldown > 0) {
                cooldown = 2;
                return;
            }

            cooldown = 2;

            var dpos = client.crosshairTarget.getPos();
            dpos = dpos.add(dpos.subtract(client.player.getEyePos()).normalize().multiply(0.01));

            var pos = new BlockPos((int)Math.floor(dpos.x), (int)Math.floor(dpos.y), (int)Math.floor(dpos.z));
            if (firstCorner) {
                AutoMinerClient.miner.StopMining();
                Miner.MiningAreaConstraint.areaLimit = null;
                firstPos = pos;

                QuickChat.ShowChat(Text.of("§aCorner 1 set"));
            }
            else {
                Miner.MiningAreaConstraint.areaLimit = new Pair<>(firstPos, pos);
                QuickChat.ShowChat(Text.of("§aCorner 2 set"));

                var size = Miner.MiningAreaConstraint.GetSizeI();

                StringBuilder str = new StringBuilder("§aArea limit set (");
                str.append(size.getX()).append("x")
                    .append(size.getY()).append("x")
                    .append(size.getZ()).append(")");
                QuickChat.ShowChat(Text.of(String.valueOf(str)));
            }

            firstCorner = !firstCorner;
        }

    }
}
