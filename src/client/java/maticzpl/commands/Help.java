package maticzpl.commands;

import maticzpl.commands.Command;
import maticzpl.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ServiceLoader;

public class Help implements Command {
    @Override
    public String ShortHelpMessage() {
        return "Shows this menu";
    }

    @Override
    public String HelpMessage() {
        return "Shows this menu";
    }

    @Override
    public String CommandName() {
        return "help";
    }

    @Override
    public String Arguments() {
        return "[command]";
    }

    @Override
    public void Execute(CommandArguments args) {
        var argument = args.NextStr();
        if (argument.isSome()) {
            args.ExpectEnd();
        }
        else {
            args.ExpectEnd();

            MutableText out = (MutableText) Text.of("");

            boolean first = true;

            for (Command cmd : Command.commands) {
                String thing = "";

                if (!first)
                    thing = "\n";
                else
                    first = false;

                out
                    .append(thing)
                    .append(cmd.CommandName())
                    .append(" ")
                    .append(cmd.Arguments())
                    .append(" - ")
                    .append(cmd.ShortHelpMessage());
            }

            QuickChat.ShowChat(out);
        }
    }
}
