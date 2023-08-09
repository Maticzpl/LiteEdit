package maticzpl.rendering;

import maticzpl.Miner;
import me.x150.renderer.render.Renderer3d;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class AreaRenderer {
    public boolean throughWalls = true;

    public void DrawBounds(MatrixStack mat) { //TODO separate this
        var area = Miner.MiningAreaConstraint;

        if (area.areaLimit == null)
            return;

        var corners = area.UpdateMinMax();

        if (throughWalls)
            Renderer3d.renderThroughWalls();

        var pos = corners.getLeft().toCenterPos().add(-0.5, -0.5, -0.5);
        DrawThickOutline(mat, Color.CYAN, pos, area.GetSize());
        DrawAxis(mat, pos);
        Renderer3d.stopRenderThroughWalls();
        DrawDoubleSided(mat, new Color(0, 255, 255, 30), pos, area.GetSize());
    }

    protected void DrawAxis(MatrixStack mat, Vec3d pos) {
        var area = Miner.MiningAreaConstraint;

        // For distant viewing
        Renderer3d.renderLine(
            mat, Color.RED, pos,
            pos.add(area.GetSize().multiply(0.5, 0, 0))
        );
        Renderer3d.renderLine(
            mat, Color.GREEN, pos,
            pos.add(area.GetSize().multiply(0, 0.5, 0))
        );
        Renderer3d.renderLine(
            mat, Color.BLUE, pos,
            pos.add(area.GetSize().multiply(0, 0, 0.5))
        );

        // Better for closeup viewing
        double t = 0.01;
        pos = pos.subtract(new Vec3d(t/2.0, t/2.0, t/2.0));

        Renderer3d.renderFilled(
            mat, Color.RED, pos,
            area.GetSize().multiply(0.5, 0, 0).add(0, t, t)
        );
        Renderer3d.renderFilled(
            mat, Color.GREEN, pos,
            area.GetSize().multiply(0, 0.5, 0).add(t, 0, t)
        );
        Renderer3d.renderFilled(
            mat, Color.BLUE, pos,
            area.GetSize().multiply(0, 0, 0.5).add(t, t, 0)
        );
    }

    protected void DrawThickOutline(MatrixStack mat, Color color, Vec3d pos, Vec3d size) {
        double t = 0.006;
        pos = pos.subtract(new Vec3d(t/2.0, t/2.0, t/2.0));
        var pos2 = pos.add(size);

        // For distant viewing
        Renderer3d.renderOutline(
            mat, color, pos, size
        );

        // Those are better for close up viewing
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
        var area = Miner.MiningAreaConstraint;

        var t = 0.001;
        pos = pos.subtract(t/2, t/2, t/2);
        var pos2 = pos.add(size);

        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos.z),
            area.GetSize().multiply(1, 0, 1).add(0, t, 0)
        );
        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos2.y, pos.z),
            area.GetSize().multiply(1, 0, 1).add(0, t, 0)
        );

        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos.z),
            area.GetSize().multiply(1, 1, 0).add(0, 0, t)
        );
        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos2.z),
            area.GetSize().multiply(1, 1, 0).add(0, 0, t)
        );

        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos.z),
            area.GetSize().multiply(0, 1, 1).add(t, 0, 0)
        );
        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos2.x, pos.y, pos.z),
            area.GetSize().multiply(0, 1, 1).add(t, 0, 0)
        );
    }
}
