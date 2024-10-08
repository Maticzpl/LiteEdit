package maticzpl;

import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class SelectionArea {
    public Pair<BlockPos,BlockPos> areaLimit = null;
    protected BlockPos minPos = new BlockPos(0,0,0);
    protected BlockPos maxPos = new BlockPos(0,0,0);

    public boolean Inside(BlockPos block) {
        if (areaLimit != null) {
            UpdateMinMax();
            if (
                block.getX() > maxPos.getX() || block.getX() < minPos.getX() ||
                block.getY() > maxPos.getY() || block.getY() < minPos.getY() ||
                block.getZ() > maxPos.getZ() || block.getZ() < minPos.getZ()
            )
                return false;
        }
        return true;
    }

    public Pair<BlockPos, BlockPos> UpdateMinMax() {
        if (areaLimit == null)
            return new Pair<>(new BlockPos(0,0,0), new BlockPos(0,0,0));

        minPos = new BlockPos(new Vec3i(
            Integer.min(areaLimit.getLeft().getX(), areaLimit.getRight().getX()),
            Integer.min(areaLimit.getLeft().getY(), areaLimit.getRight().getY()),
            Integer.min(areaLimit.getLeft().getZ(), areaLimit.getRight().getZ())
        ));
        maxPos = new BlockPos(new Vec3i(
            Integer.max(areaLimit.getLeft().getX(), areaLimit.getRight().getX()),
            Integer.max(areaLimit.getLeft().getY(), areaLimit.getRight().getY()),
            Integer.max(areaLimit.getLeft().getZ(), areaLimit.getRight().getZ())
        ));
        return new Pair<>(minPos, maxPos);
    }



    public Vec3d GetSize() {
        if (areaLimit == null)
            return new Vec3d(0,0,0);

        var l = areaLimit.getLeft();
        var r = areaLimit.getRight();

        int sx = Math.abs(l.getX() - r.getX()) + 1;
        int sy = Math.abs(l.getY() - r.getY()) + 1;
        int sz = Math.abs(l.getZ() - r.getZ()) + 1;
        return new Vec3d(sx, sy, sz);
    }

    public Vec3i GetSizeI() {
        var s = GetSize();
        return new Vec3i((int) s.getX(), (int) s.getY(), (int) s.getZ());
    }

    @Override
    public String toString() {
        if (areaLimit == null) {
            return "No area selected";
        }

        var l = areaLimit.getLeft();
        var r = areaLimit.getRight();

        var size = GetSizeI();
        int sx = size.getX();
        int sy = size.getY();
        int sz = size.getZ();

        return "Selected area:" + l.getX() + " " + l.getY() + " " + l.getZ() + " - " +
                r.getX() + " " + r.getY() + " " + r.getZ() +
                " ("+sx+"x"+sy+"x"+sz+")";
    }
}
