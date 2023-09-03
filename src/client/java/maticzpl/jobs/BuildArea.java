package maticzpl.jobs;

import maticzpl.Builder;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class BuildArea extends JobArea{
    public Block buildingBlock;

    public BuildArea(Pair<BlockPos, BlockPos> area, Block block, Color col) {
        super(area, col);
        buildingBlock = block;
    }

    @Override
    public boolean Do(BlockPos pos, double dist) {
        if (!Inside(pos))
            return false;

        if (buildingBlock == null)
            return false;

        var client = MinecraftClient.getInstance();

        var blockState = client.world.getBlockState(pos);
        if (blockState.getBlock().getDefaultState().equals(buildingBlock.getDefaultState()))
            return true;
        if (blockState.isAir() || blockState.isReplaceable()) {
            Builder.PlaceBlock(buildingBlock, pos);
            return true;
        }

        return false;
    }


}
