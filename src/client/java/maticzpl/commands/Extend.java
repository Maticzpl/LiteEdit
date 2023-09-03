package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.AnyStrArg;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.IntArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

public class Extend implements Command {
    protected EmptyArg[] argTree;

    public Extend() {
        var dist = new IntArg("dist", EmptyArg.End).arr();

        // up down right left forward back
//        var allowed = new ArrayList<String>();
//        allowed.add("+x");
//        allowed.add("-x");
//        allowed.add("+y");
//        allowed.add("-y");
//        allowed.add("+z");
//        allowed.add("-z");
//
//        var direction = new StrArg(allowed, "", dist);

        // old way
        var r = new StrArg("+x", "", dist);
        var l = new StrArg("-x", "", dist);
        var u = new StrArg("+y", "", dist);
        var d = new StrArg("-y", "", dist);
        var f = new StrArg("+z", "", dist);
        var b = new StrArg("-z", "", dist);
        var comb = new AnyStrArg("combination", dist);

        dist[0].AddCallback(data -> {
            var di = (int)data.pop();
            boolean reversed = di < 0;

            if (reversed)
                di = -di;

            var previousCorners = Builder.selection.UpdateMinMax();
            if (Builder.selection.areaLimit == null)
                previousCorners = null;

            try {
                var directions = (String)data.pop();
                for (int i = 0; i < directions.length(); i+=2) {
                    var direction = directions.substring(i, Math.min(i+2, directions.length()));
                    switch (direction) {
                        case "+x" -> Move(reversed, di, 0, 0);
                        case "-x" -> Move(reversed, -di, 0, 0);
                        case "+y" -> Move(reversed, 0, di, 0);
                        case "-y" -> Move(reversed, 0, -di, 0);
                        case "+z" -> Move(reversed, 0, 0, di);
                        case "-z" -> Move(reversed, 0, 0, -di);
                        default -> {
                            QuickChat.ShowChat(Text.of("§cWrong direction '" + direction + "'"));
                            throw new Exception();
                        }
                    }
                }
            }
            catch (Exception e) {
                Builder.selection.areaLimit = previousCorners;
                return;
            }

            var size = Builder.selection.GetSizeI();

            String str = "§aArea selected (" + size.getX() + "x" +
                    size.getY() + "x" +
                    size.getZ() + ")";

            QuickChat.ShowChat(Text.of(str));
        });

        argTree = new EmptyArg[] {
            //direction
            u, d, r, l, f, b, comb
        };
    }

    protected void Move(boolean reverse, int x, int y, int z) throws Exception {
        var area = Builder.selection;

        if (area.areaLimit == null) {
            QuickChat.ShowChat(Text.of("§cCan't extend nonexistent selection area"));
            return;
        }

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
        }
        else {
            QuickChat.ShowChat(Text.of("§cResulting selection too small"));
            throw new Exception();
        }
    }

    @Override
    public String ShortHelpMessage() {
        return "Extends selection area in provided direction";
    }

    @Override
    public String HelpMessage() {
        return "Extends selection area in provided direction by the provided amount of blocks.\n Multiple directions can be provided like +x-x+z-z\n Negative distance can be provided in order to shrink the area";
    }

    @Override
    public String CommandName() {
        return "extend";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }

}
