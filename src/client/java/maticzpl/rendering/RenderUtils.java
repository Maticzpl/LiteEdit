package maticzpl.rendering;

import me.x150.renderer.render.Renderer3d;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class RenderUtils {
    public static void DrawThickOutline(MatrixStack mat, Color color, Vec3d pos, Vec3d size, double thickness) {
        double t = 0.006 * thickness;
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

    public static void DrawDoubleSided(MatrixStack mat, Color color, Vec3d pos, Vec3d size, double thickness) {
        var t = 0.001 * thickness;
        pos = pos.subtract(t/2, t/2, t/2);
        var pos2 = pos.add(size);

        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos.z),
            size.multiply(1, 0, 1).add(0, t, 0)
        );
        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos2.y, pos.z),
            size.multiply(1, 0, 1).add(0, t, 0)
        );

        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos.z),
            size.multiply(1, 1, 0).add(0, 0, t)
        );
        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos2.z),
            size.multiply(1, 1, 0).add(0, 0, t)
        );

        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos.x, pos.y, pos.z),
            size.multiply(0, 1, 1).add(t, 0, 0)
        );
        Renderer3d.renderFilled(
            mat, color,
            new Vec3d(pos2.x, pos.y, pos.z),
            size.multiply(0, 1, 1).add(t, 0, 0)
        );
    }
}
