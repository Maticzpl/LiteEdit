package maticzpl.jobs;

import maticzpl.Builder;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;

public class MineArea extends JobArea{
    public ArrayList<Block> ignoredBlocks = new ArrayList<>();

    public MineArea(Pair<BlockPos, BlockPos> area, Color col) {
        super(area, col);
    }

    public MineArea(Pair<BlockPos, BlockPos> area, ArrayList<Block> ignore, Color col) {
        super(area, col);
        ignoredBlocks = ignore;
    }

    @Override
    public boolean Do(BlockPos pos, double dist) {
        if (!Inside(pos))
            return false;

        var client = MinecraftClient.getInstance();

        if (ignoredBlocks.contains(client.world.getBlockState(pos).getBlock()))
            return false;

        Builder.BreakBlock(pos, dist);

        return true;
    }


}
