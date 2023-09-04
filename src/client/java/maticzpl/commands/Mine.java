package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.BlockArg;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.jobs.BuildArea;
import maticzpl.jobs.MineArea;
import maticzpl.utils.QuickChat;
import net.minecraft.block.Block;
import net.minecraft.text.Text;

import java.awt.*;


public class Mine implements Command {
    EmptyArg[] argTree;

    public Mine() {
        var run = new EmptyArg(EmptyArg.End);
        var ignore = new BlockArg("ignore", run.arr());

        run.AddCallback(data -> {

            if (Builder.selection.areaLimit == null) {
                QuickChat.ShowChat(Text.of("§cNeed to select area to be mined"));
                return;
            }

            Builder.jobs.add(new MineArea(Builder.selection.areaLimit, Color.RED));

//            QuickChat.ShowChat(Text.of("§aArea designated for mining"));
        });

        argTree = new EmptyArg[] {
            run
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Designates selected area to be mined";
    }

    @Override
    public String HelpMessage() {
        return "Designates selected area to be mined";
    }

    @Override
    public String CommandName() {
        return "mine";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
