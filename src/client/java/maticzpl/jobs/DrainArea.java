package maticzpl.jobs;

import maticzpl.Builder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;

public class DrainArea extends JobArea{

    public DrainArea(Pair<BlockPos, BlockPos> area, Color col) {
        super(area, col);
    }

    @Override
    public boolean Do(BlockPos pos, double dist) {
        if (!Inside(pos))
            return false;

        var client = MinecraftClient.getInstance();

        var sponge = Registries.BLOCK.get(new Identifier("minecraft:sponge"));

        var sponges = new ArrayList<BlockState>();
        sponges.add(sponge.getDefaultState());
        sponges.add(Registries.BLOCK.get(new Identifier("minecraft:wet_sponge")).getDefaultState());

        var blockState = client.world.getBlockState(pos);
        if (sponges.contains(blockState.getBlock().getDefaultState())) {
            Builder.BreakBlock(pos, dist);
            return true;
        }
        if (blockState.getBlock().equals(Registries.BLOCK.get(new Identifier("minecraft:water")))) {
            Builder.PlaceBlock(sponge, pos);
            return true;
        }

        return false;
    }


}
