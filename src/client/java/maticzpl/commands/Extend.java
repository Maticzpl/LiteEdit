package maticzpl.commands;

import maticzpl.Miner;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.IntArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3i;

public class Extend implements Command {
    protected EmptyArg[] argTree;

    public Extend() {
        var dist = new IntArg(EmptyArg.End);

        // up down right left forward back
        var r = new StrArg("+x", dist);
        var l = new StrArg("-x", dist);
        var u = new StrArg("+y", dist);
        var d = new StrArg("-y", dist);
        var f = new StrArg("+z", dist);
        var b = new StrArg("-z", dist);

        dist.AddCallback(data -> {
            var di = (int)data.pop();
            boolean reversed = di < 0;

            if (reversed)
                di = -di;

            switch ((String)data.pop()) {
                case "+x": Move(reversed, di, 0, 0); break;
                case "-x": Move(reversed, -di, 0, 0); break;
                case "+y": Move(reversed, 0, di, 0); break;
                case "-y": Move(reversed, 0, -di, 0); break;
                case "+z": Move(reversed, 0, 0, di); break;
                case "-z": Move(reversed, 0, 0, -di); break;
            }
        });

        argTree = new EmptyArg[] {
            u, d, r, l, f, b
        };
    }

    protected void Move(boolean reverse, int x, int y, int z) {
        var area = Miner.MiningAreaConstraint;
        var corners = area.UpdateMinMax();

        if (x > 0) {
            if (reverse)
                x = -x;
            corners.setRight(corners.getRight().add(x, 0, 0));
        }
        else if (x < 0) {
            if (reverse)
                x = -x;
            corners.setLeft(corners.getLeft().add(x, 0, 0));
        }

        if (y > 0) {
            if (reverse)
                y = -y;
            corners.setRight(corners.getRight().add(0, y, 0));
        }
        else if (y < 0) {
            if (reverse)
                y = -y;
            corners.setLeft(corners.getLeft().add(0, y, 0));
        }

        if (z > 0) {
            if (reverse)
                z = -z;
            corners.setRight(corners.getRight().add(0, 0, z));
        }
        else if (z < 0) {
            if (reverse)
                z = -z;
            corners.setLeft(corners.getLeft().add(0, 0, z));
        }

        if (
            corners.getLeft().getX() <= corners.getRight().getX() &&
            corners.getLeft().getY() <= corners.getRight().getY() &&
            corners.getLeft().getZ() <= corners.getRight().getZ()
        ) {
            area.areaLimit = corners;

            var size = Miner.MiningAreaConstraint.GetSizeI();

            String str = "§aArea limit set (" + size.getX() + "x" +
                size.getY() + "x" +
                size.getZ() + ")";

            QuickChat.ShowChat(Text.of(str));
        }
        else {
            QuickChat.ShowChat(Text.of("§cResulting area too small"));
        }
    }

    @Override
    public String ShortHelpMessage() {
        return "Extends area in provided direction";
    }

    @Override
    public String HelpMessage() {
        return "Extends area in provided direction by the provided amount of blocks";
    }

    @Override
    public String CommandName() {
        return "extend";
    }

    @Override
    public String Arguments() {
        return "<+x | -x | +y | -y | +z | -z> <distance>";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }

}
