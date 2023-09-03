package maticzpl.jobs;

import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.awt.*;

public abstract class JobArea {
    public BlockPos minPos;
    public BlockPos maxPos;
    public Color color;

    public JobArea(Pair<BlockPos,BlockPos> area, Color col) {
        minPos = new BlockPos(new Vec3i(
            Integer.min(area.getLeft().getX(), area.getRight().getX()),
            Integer.min(area.getLeft().getY(), area.getRight().getY()),
            Integer.min(area.getLeft().getZ(), area.getRight().getZ())
        ));
        maxPos = new BlockPos(new Vec3i(
            Integer.max(area.getLeft().getX(), area.getRight().getX()),
            Integer.max(area.getLeft().getY(), area.getRight().getY()),
            Integer.max(area.getLeft().getZ(), area.getRight().getZ())
        ));
        color = col;
    }

    public Vec3d GetSize() {
        var l = minPos;
        var r = maxPos;

        int sx = Math.abs(l.getX() - r.getX()) + 1;
        int sy = Math.abs(l.getY() - r.getY()) + 1;
        int sz = Math.abs(l.getZ() - r.getZ()) + 1;
        return new Vec3d(sx, sy, sz);
    }

    public boolean Inside(BlockPos pos) {
        return pos.getX() <= maxPos.getX() && pos.getX() >= minPos.getX() &&
                pos.getY() <= maxPos.getY() && pos.getY() >= minPos.getY() &&
                pos.getZ() <= maxPos.getZ() && pos.getZ() >= minPos.getZ();
    }

    // If returns true, stops further jobs from running in this position
    public abstract boolean Do(BlockPos pos, double dist);
}
