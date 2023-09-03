package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.utils.QuickChat;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.ClickEvent;

public class Jobs implements Command {
    EmptyArg[] argTree;
    MessageSignatureData previous;

    public Jobs() {
        var nothing = new EmptyArg(EmptyArg.End);
        var clear = new StrArg("clear", "clear", EmptyArg.End);

        clear.AddCallback(data -> {
            Builder.jobs.clear();
        });

        nothing.AddCallback(data -> {
            if (previous != null)
                QuickChat.Clear(previous);

            previous = QuickChat.ShowChat(new QuickChat.Msg[]{
                new QuickChat.Msg("§6[ §eJobs §6]"),
                new QuickChat.Msg("\n§e [Clear] ", new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$jobs clear")),
                new QuickChat.Msg("\n §lList\n"),
            });
        });

        argTree = new EmptyArg[] {
            nothing,
            clear
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "TODO";
    }

    @Override
    public String HelpMessage() {
        return "TODO";
    }

    @Override
    public String CommandName() {
        return "jobs";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
