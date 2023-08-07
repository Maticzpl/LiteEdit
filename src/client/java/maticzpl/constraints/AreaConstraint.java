package maticzpl.constraints;

import me.x150.renderer.render.Renderer3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.awt.*;

public class AreaConstraint implements Constraint {
    public Pair<BlockPos,BlockPos> areaLimit = null;
    public boolean throughWalls = true;
    protected BlockPos minPos = new BlockPos(0,0,0);
    protected BlockPos maxPos = new BlockPos(0,0,0);

    @Override
    public boolean Allowed(BlockPos block) {
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

    protected void UpdateMinMax() {
        if (areaLimit == null)
            return;

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
    }

    public void DrawBounds(MatrixStack mat) { //TODO separate this
        if (areaLimit == null)
            return;

        UpdateMinMax();

        if (throughWalls)
            Renderer3d.renderThroughWalls();

        Renderer3d.renderEdged(
            mat,
            new Color(0,0,0,0),
            Color.CYAN,
            minPos.toCenterPos().add(-0.49, -0.49, -0.49), // no clipping
            GetSize().subtract(new Vec3d(0.02, 0.02, 0.02))
        );

        Renderer3d.stopRenderThroughWalls();
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
            return "Area limit not set";
        }

        var l = areaLimit.getLeft();
        var r = areaLimit.getRight();

        var size = GetSizeI();
        int sx = size.getX();
        int sy = size.getY();
        int sz = size.getZ();

        return "Area limit:" + l.getX() + " " + l.getY() + " " + l.getZ() + " - " +
                r.getX() + " " + r.getY() + " " + r.getZ() +
                " ("+sx+"x"+sy+"x"+sz+")";
    }
}
