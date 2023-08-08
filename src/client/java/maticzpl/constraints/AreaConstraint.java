package maticzpl.constraints;

import me.x150.renderer.render.Renderer2d;
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

    public void DrawBounds(MatrixStack mat) { //TODO separate this
        if (areaLimit == null)
            return;

        UpdateMinMax();

        if (throughWalls)
            Renderer3d.renderThroughWalls();

        var pos = minPos.toCenterPos().add(-0.5, -0.5, -0.5);
        DrawThickOutline(mat, Color.CYAN, pos, GetSize());
        DrawAxis(mat, pos);
        Renderer3d.stopRenderThroughWalls();
        DrawDoubleSided(mat, new Color(0, 255, 255, 30), pos, GetSize());

    }

    protected void DrawAxis(MatrixStack mat, Vec3d pos) {
        // thickness
        double t = 0.01;

        pos = pos.subtract(new Vec3d(t/2.0, t/2.0, t/2.0));

        Renderer3d.renderFilled(
            mat,
            Color.RED,
            pos,
            GetSize().multiply(0.5, 0, 0).add(0, t, t)
        );
        Renderer3d.renderFilled(
            mat,
            Color.GREEN,
            pos,
            GetSize().multiply(0, 0.5, 0).add(t, 0, t)
        );
        Renderer3d.renderFilled(
            mat,
            Color.BLUE,
            pos,
            GetSize().multiply(0, 0, 0.5).add(t, 0, t)
        );
    }

    protected void DrawThickOutline(MatrixStack mat, Color color, Vec3d pos, Vec3d size) {
        double t = 0.006;
        pos = pos.subtract(new Vec3d(t/2.0, t/2.0, t/2.0));
        var pos2 = pos.add(size);

        Renderer3d.renderFilled(
            mat, color, new Vec3d(pos.x, pos.y, pos.z),
            size.multiply(1, 0, 0).add(0, t, t)
        );
        Renderer3d.renderFilled(
            mat, color, new Vec3d(pos.x, pos.y, pos.z),
            size.multiply(0, 1, 0).add(t, 0, t)
        );
        Renderer3d.renderFilled(
            mat, color, new Vec3d(pos.x, pos2.y, pos.z),
            size.multiply(1, 0, 0).add(0, t, t)
        );
        Renderer3d.renderFilled(
            mat, color, new Vec3d(pos2.x, pos.y, pos.z),
            size.multiply(0, 1, 0).add(t, 0, t)
        );

        Renderer3d.renderFilled(
                mat, color, new Vec3d(pos.x, pos.y, pos2.z),
                size.multiply(1, 0, 0).add(0, t, t)
        );
        Renderer3d.renderFilled(
                mat, color, new Vec3d(pos.x, pos.y, pos2.z),
                size.multiply(0, 1, 0).add(t, 0, t)
        );
        Renderer3d.renderFilled(
                mat, color, new Vec3d(pos.x, pos2.y, pos2.z),
                size.multiply(1, 0, 0).add(0, t, t)
        );
        Renderer3d.renderFilled(
                mat, color, new Vec3d(pos2.x, pos.y, pos2.z),
                size.multiply(0, 1, 0).add(t, 0, t)
        );

        Renderer3d.renderFilled(
                mat, color, new Vec3d(pos.x, pos2.y, pos.z),
                size.multiply(0, 0, 1).add(t, t, 0)
        );
        Renderer3d.renderFilled(
                mat, color, new Vec3d(pos2.x, pos2.y, pos.z),
                size.multiply(0, 0, 1).add(t, t, 0)
        );
        Renderer3d.renderFilled(
                mat, color, new Vec3d(pos.x, pos.y, pos.z),
                size.multiply(0, 0, 1).add(t, t, 0)
        );
        Renderer3d.renderFilled(
                mat, color, new Vec3d(pos2.x, pos.y, pos.z),
                size.multiply(0, 0, 1).add(t, t, 0)
        );

    }

    protected void DrawDoubleSided(MatrixStack mat, Color color, Vec3d pos, Vec3d size) {
        var t = 0.001;
        pos = pos.subtract(t/2, t/2, t/2);
        var pos2 = pos.add(size);

        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos.z),
            GetSize().multiply(1, 0, 1).add(0, t, 0)
        );
        Renderer3d.renderFilled(
                mat, color,
                new Vec3d(pos.x, pos2.y, pos.z),
                GetSize().multiply(1, 0, 1).add(0, t, 0)
        );

        Renderer3d.renderFilled(
                mat, color,
                new Vec3d(pos.x, pos.y, pos.z),
                GetSize().multiply(1, 1, 0).add(0, 0, t)
        );
        Renderer3d.renderFilled(
                mat, color,
                new Vec3d(pos.x, pos.y, pos2.z),
                GetSize().multiply(1, 1, 0).add(0, 0, t)
        );

        Renderer3d.renderFilled(
                mat, color,
                new Vec3d(pos.x, pos.y, pos.z),
                GetSize().multiply(0, 1, 1).add(t, 0, 0)
        );
        Renderer3d.renderFilled(
                mat, color,
                new Vec3d(pos2.x, pos.y, pos.z),
                GetSize().multiply(0, 1, 1).add(t, 0, 0)
        );
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
