package maticzpl.constraints;

import maticzpl.Builder;
import maticzpl.Miner;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class PlaceConstraint implements Constraint{
    public Block currentBlock;

    @Override
    public boolean Allowed(BlockPos pos) {
        var client = MinecraftClient.getInstance();

        if (currentBlock == null || Miner.MiningAreaConstraint.areaLimit == null)
            return true;

        if (client.world.getBlockState(pos).getBlock().getDefaultState().equals(currentBlock.getDefaultState()))
            return false;
        if (client.world.getBlockState(pos).isAir()) {
            Builder.PlaceBlock(currentBlock, pos);
            return false;
        }
        return true;
    }
}
