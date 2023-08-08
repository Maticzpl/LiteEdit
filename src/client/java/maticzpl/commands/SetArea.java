package maticzpl.commands;

import maticzpl.Miner;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.IntArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class SetArea implements Command {
    protected EmptyArg[] argTree;

    public SetArea() {
        var allowed = new ArrayList<String>();
        allowed.add("clear");

        var clear = new StrArg(allowed, EmptyArg.End);

        clear.AddCallback(data -> {
            Miner.MiningAreaConstraint.areaLimit = null;
            QuickChat.ShowChat(Text.of("§aArea limit cleared"));
        });

        // X Y Z X1 Y1 Z1 END
        var Z1 = new IntArg(EmptyArg.End);
        var coords = new IntArg(new IntArg(new IntArg(new IntArg(new IntArg(Z1)))));

        Z1.AddCallback(data -> {
            int z1 = (int)data.pop();
            int y1 = (int)data.pop();
            int x1 = (int)data.pop();
            BlockPos second = new BlockPos(x1, y1, z1);

            int z = (int)data.pop();
            int y = (int)data.pop();
            int x = (int)data.pop();
            BlockPos first = new BlockPos(x, y, z);

            Miner.MiningAreaConstraint.areaLimit = new Pair<>(first, second);

            var size = Miner.MiningAreaConstraint.GetSizeI();

            String str = "§aArea limit set (" + size.getX() + "x" +
                size.getY() + "x" +
                size.getZ() + ")";

            QuickChat.ShowChat(Text.of(str));
        });

        argTree = new EmptyArg[] {
            clear, coords
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Limits the mining area";
    }

    @Override
    public String HelpMessage() {
        return "If given coordinates, prevents the autominer from breaking blocks outside of the specified area. If given clear, the area limit will be removed.";
    }

    @Override
    public String CommandName() {
        return "setarea";
    }

    @Override
    public String Arguments() {
        return "<x> <y> <z> <x1> <y1> <z1> | 'clear'";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }

}
