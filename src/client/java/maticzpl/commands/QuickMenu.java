package maticzpl.commands;

import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.ClickEvent;

public class QuickMenu implements Command {
    EmptyArg[] argTree;

    public QuickMenu() {
        var nothing = new EmptyArg(EmptyArg.End);

        nothing.AddCallback(data -> {
            QuickChat.ShowChat(new QuickChat.Msg[]{
                new QuickChat.Msg("§e---===[§lQuick Menu§r§e]===---\n"),
                new QuickChat.Msg("§e§lMisc\n"),
                new QuickChat.Msg("§e[Platform]", new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$platform")),
            });
        });

        argTree = new EmptyArg[] {
            nothing
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Quick menu with clickable buttons for various stuff";
    }

    @Override
    public String HelpMessage() {
        return "Quick menu with clickable buttons for various stuff";
    }

    @Override
    public String CommandName() {
        return "qm";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
