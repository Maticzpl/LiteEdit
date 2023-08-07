package maticzpl.commands;

import maticzpl.commands.Command;
import maticzpl.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ServiceLoader;

public class Help implements Command {
    @Override
    public String ShortHelpMessage() {
        return "Shows this menu";
    }

    @Override
    public String HelpMessage() {
        return "Shows this menu or longer help message for the specified command";
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
        if (!args.IsNextEmpty()) {
            var commandName = args.NextStr().unwrap().toLowerCase();
            args.ExpectEnd();

            for (Command cmd : Command.commands) {
                if (cmd.CommandName().equals(commandName)) {
                    MutableText txt = (MutableText)Text.of(
                            "§6" + cmd.CommandName() + "§o " + cmd.Arguments() + "§r - §e" + cmd.HelpMessage()
                    );
                    txt.formatted(Formatting.YELLOW);
                    QuickChat.ShowChat(txt);
                    return;
                }
            }

            MutableText txt = (MutableText)Text.of("Command not found");
            txt.formatted(Formatting.GOLD);
            QuickChat.ShowChat(txt);
        }
        else {
            args.ExpectEnd();

            MutableText out = (MutableText) Text.of("");

            boolean first = true;

            for (Command cmd : Command.commands) {
                String thing = "§6";

                if (!first)
                    thing = "\n§6";
                else
                    first = false;

                out.append(Text.of(thing + cmd.CommandName() + "§o " + cmd.Arguments() + "§r - §e" + cmd.ShortHelpMessage()));
            }

            QuickChat.ShowChat(out);
        }
    }
}
