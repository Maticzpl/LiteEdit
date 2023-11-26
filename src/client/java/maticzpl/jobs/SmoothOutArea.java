package maticzpl.jobs;

import maticzpl.Builder;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;

public class SmoothOutArea extends JobArea{
    public ArrayList<Block> ignoredBlocks = new ArrayList<>();

    public SmoothOutArea(Pair<BlockPos, BlockPos> area, Color col) {
        super(area, col);
    }

    public SmoothOutArea(Pair<BlockPos, BlockPos> area, ArrayList<Block> ignore, Color col) {
        super(area, col);
        ignoredBlocks = ignore;
    }

    @Override
    public boolean Do(BlockPos pos, double dist) {
        if (!Inside(pos))
            return false;

        var client = MinecraftClient.getInstance();

        if (ignoredBlocks.contains(client.world.getBlockState(pos).getBlock()) ||
                client.world.getBlockState(pos).isAir())
            return false;

        // 33 threshold for r = 2
        int r = 1;
        int neighbours = -1; //cause self
        int threshold = 12;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    if (!client.world.getBlockState(pos.add(x, y, z)).isAir())
                        neighbours++;
                }
            }
        }

        if (neighbours < threshold) {
            Builder.BreakBlock(pos, dist);
            return true;
        }

        return false;
    }


}
