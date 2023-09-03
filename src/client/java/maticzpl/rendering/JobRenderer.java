package maticzpl.rendering;

import maticzpl.Builder;
import me.x150.renderer.render.Renderer3d;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class JobRenderer {
    public static void Draw(MatrixStack mat) {
        for (var job : Builder.jobs) {

            if (AreaRenderer.throughWalls)
                Renderer3d.renderThroughWalls();

            var c = job.color;
            var pos = job.minPos.toCenterPos().add(-0.5, -0.5, -0.5);
            RenderUtils.DrawThickOutline(mat, new Color(c.getRed(), c.getGreen(), c.getBlue(), 128), pos, job.GetSize(), 0.1);
            Renderer3d.stopRenderThroughWalls();

            RenderUtils.DrawDoubleSided(mat, new Color(c.getRed(), c.getGreen(), c.getBlue(), 10), pos, job.GetSize(), 0.1);
        }
    }
}
