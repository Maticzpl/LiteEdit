package maticzpl.commands;

import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.utils.QuickChat;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.ClickEvent;

public class QuickMenu implements Command {
    EmptyArg[] argTree;
    MessageSignatureData previous;

    public QuickMenu() {
        var nothing = new EmptyArg(EmptyArg.End);

        nothing.AddCallback(data -> {
            if (previous != null)
                QuickChat.Clear(previous);

            previous = QuickChat.ShowChat(new QuickChat.Msg[]{
                new QuickChat.Msg("§6[ §eQuick Menu §6]"),
                new QuickChat.Msg("\n §lArea\n"),
                new QuickChat.Msg("§e  [clear] ", new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$setarea clear")),
                new QuickChat.Msg("\n §lArea Extend\n"),
                new QuickChat.Msg("  §e[+x] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend +x 1")),
                new QuickChat.Msg("  §e[+y] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend +y 1")),
                new QuickChat.Msg("  §e[+z] \n",  new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend +z 1")),
                new QuickChat.Msg("  §e[-x] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend -x 1")),
                new QuickChat.Msg("  §e[-y] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend -y 1")),
                new QuickChat.Msg("  §e[-z] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend -z 1")),
                new QuickChat.Msg("\n §lArea Shrink\n"),
                new QuickChat.Msg("  §e[+x] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend +x -1")),
                new QuickChat.Msg("  §e[+y] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend +y -1")),
                new QuickChat.Msg("  §e[+z] \n",  new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend +z -1")),
                new QuickChat.Msg("  §e[-x] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend -x -1")),
                new QuickChat.Msg("  §e[-y] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend -y -1")),
                new QuickChat.Msg("  §e[-z] ",    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$extend -z -1")),
                new QuickChat.Msg("\n §lMisc\n"),
                new QuickChat.Msg("  §e[Platform] ",  new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$platform")),
                new QuickChat.Msg("  §e[Make Wand] ", new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$wand")),
                new QuickChat.Msg("\n§6[ §eMenu End §6]"),
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
