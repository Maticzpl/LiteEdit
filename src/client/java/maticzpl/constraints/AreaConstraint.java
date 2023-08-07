package maticzpl.constraints;

import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class AreaConstraint implements Constraint {
    public Pair<BlockPos,BlockPos> areaLimit = null;

    @Override
    public boolean Allowed(BlockPos block) {
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
                block.getX() > maxPos.getX() || block.getX() < minPos.getX() ||
                block.getY() > maxPos.getY() || block.getY() < minPos.getY() ||
                block.getZ() > maxPos.getZ() || block.getZ() < minPos.getZ()
            )
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (areaLimit == null) {
            return "Area limit not set";
        }

        var l = areaLimit.getLeft();
        var r = areaLimit.getRight();

        int sx = Math.abs(l.getX() - r.getX());
        int sy = Math.abs(l.getY() - r.getY());
        int sz = Math.abs(l.getZ() - r.getZ());


        return "Area limit:" + l.getX() + " " + l.getY() + " " + l.getZ() + " - " +
                r.getX() + " " + r.getY() + " " + r.getZ() +
                " ("+sx+"x"+sy+"x"+sz+")";
    }
}
