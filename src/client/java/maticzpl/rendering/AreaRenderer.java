package maticzpl.rendering;

import maticzpl.Builder;
import me.x150.renderer.render.Renderer3d;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class AreaRenderer {
    public static boolean throughWalls = true; //move to better common place

    public void DrawBounds(MatrixStack mat) { //TODO separate this
        var area = Builder.selection;

        if (area.areaLimit == null)
            return;

        var corners = area.UpdateMinMax();

        if (throughWalls)
            Renderer3d.renderThroughWalls();

        var pos = corners.getLeft().toCenterPos().add(-0.5, -0.5, -0.5);
        RenderUtils.DrawThickOutline(mat, Color.CYAN, pos, area.GetSize(), 1.0);
        DrawAxis(mat, pos);
        Renderer3d.stopRenderThroughWalls();
        RenderUtils.DrawDoubleSided(mat, new Color(0, 255, 255, 30), pos, area.GetSize(), 1.0);
    }

    public void DrawAxis(MatrixStack mat, Vec3d pos) {
        var area = Builder.selection;

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


}
