package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.BlockArg;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.jobs.BuildArea;
import maticzpl.jobs.DrainArea;
import maticzpl.jobs.MineArea;
import maticzpl.utils.QuickChat;
import net.minecraft.block.Block;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;


public class Drain implements Command {
    EmptyArg[] argTree;

    public Drain() {
        var run = new EmptyArg(EmptyArg.End);

        run.AddCallback(data -> {
            if (Builder.selection.areaLimit == null) {
                QuickChat.ShowChat(Text.of("§cNeed to select area to be drained"));
                return;
            }

            Builder.jobs.add(new DrainArea(Builder.selection.areaLimit, Color.MAGENTA));

            QuickChat.ShowChat(Text.of("§aArea will be drained of water"));
        });

        argTree = new EmptyArg[] {
            run
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Designates selected area to be drained of water";
    }

    @Override
    public String HelpMessage() {
        return "Designates selected area to be drained of water";
    }

    @Override
    public String CommandName() {
        return "drain";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
