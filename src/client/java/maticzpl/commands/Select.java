package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.LiteEditClient;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.IntArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class Select implements Command {
    protected EmptyArg[] argTree;

    public Select() {
        var allowed = new ArrayList<String>();
        allowed.add("none");

        var clear = new StrArg(allowed, "", EmptyArg.End);

        clear.AddCallback(data -> {
            LiteEditClient.miner.StopMining();
            Builder.MiningAreaConstraint.areaLimit = null;
            QuickChat.ShowChat(Text.of("§aSelection cleared"));
        });

        var Z1 = new IntArg("z1", EmptyArg.End);
        var coords = new IntArg("x", new IntArg("y", new IntArg("z", new IntArg("x1", new IntArg("y1", Z1.arr()).arr()).arr()).arr()).arr());

        Z1.AddCallback(data -> {
            int z1 = (int)data.pop();
            int y1 = (int)data.pop();
            int x1 = (int)data.pop();
            BlockPos second = new BlockPos(x1, y1, z1);

            int z = (int)data.pop();
            int y = (int)data.pop();
            int x = (int)data.pop();
            BlockPos first = new BlockPos(x, y, z);

            Builder.MiningAreaConstraint.areaLimit = new Pair<>(first, second);

            var size = Builder.MiningAreaConstraint.GetSizeI();

            String str = "§aArea selected (" + size.getX() + "x" +
                size.getY() + "x" +
                size.getZ() + ")";

            QuickChat.ShowChat(Text.of(str));
        });

        argTree = new EmptyArg[] {
            coords, clear
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Selects provided area. Does nothing on its own";
    }

    @Override
    public String HelpMessage() {
        return "If given coordinates, selects the specified area for further operations. If given none, the area selection will be removed.";
    }

    @Override
    public String CommandName() {
        return "select";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }

}
