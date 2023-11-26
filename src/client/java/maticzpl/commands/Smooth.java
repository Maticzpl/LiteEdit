package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.jobs.DrainArea;
import maticzpl.jobs.SmoothOutArea;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

import java.awt.*;


public class Smooth implements Command {
    EmptyArg[] argTree;

    public Smooth() {
        var run = new EmptyArg(EmptyArg.End);

        run.AddCallback(data -> {
            if (Builder.selection.areaLimit == null) {
                QuickChat.ShowChat(Text.of("§cNeed to select area to be smoothed"));
                return;
            }

            Builder.jobs.add(new SmoothOutArea(Builder.selection.areaLimit, Color.MAGENTA));

            QuickChat.ShowChat(Text.of("§aArea will be smoothened"));
        });

        argTree = new EmptyArg[] {
            run
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Designates selected area to be smoothed";
    }

    @Override
    public String HelpMessage() {
        return "Designates selected area to be smoothed";
    }

    @Override
    public String CommandName() {
        return "smooth";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
