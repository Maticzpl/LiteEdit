package maticzpl.commands;

import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.IntArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Consumer;

public class Help implements Command {
    protected static MessageSignatureData previousList = null;
    protected static MessageSignatureData previous = null;
    protected EmptyArg[] argTree;

    protected void LazyInit() {
        if (argTree != null)
            return;

        var allowed = new ArrayList<String>();
        for (Command cmd : Command.commands) {
            allowed.add(cmd.CommandName());
        }

        var gaveName = new StrArg(allowed, "command", EmptyArg.End);
        var listAll = new EmptyArg(EmptyArg.End);
        var page = new IntArg("page", EmptyArg.End);

        gaveName.AddCallback(data -> {
            var name = (String)data.pop();

            for (Command cmd : Command.commands) {
                if (cmd.CommandName().equals(name)) {
                    MutableText txt = (MutableText)Text.of(
                        "§e" + cmd.CommandName() + "§6§o " + cmd.GetArgStr() + "§r - §7" + cmd.HelpMessage()
                    );
                    txt.formatted(Formatting.YELLOW);

                    if (previous != null) {
                        QuickChat.Clear(previous);
                    }

                    previous = QuickChat.ShowChat(txt);
                    return;
                }
            }

            MutableText txt = (MutableText)Text.of("Command not found");
            txt.formatted(Formatting.GOLD);
            QuickChat.ShowChat(txt);
        });

        Consumer<Stack<Object>> callback = data -> {
            int pageNum = 0;
            if(!data.empty()) {
                var val = data.pop();
                if (val != null)
                    pageNum = (Integer) val;
            }
            pageNum = Math.max(0, pageNum);

            MutableText out = (MutableText) Text.of("");

            boolean first = true;
            boolean reachedEnd = false;

            var lineLimit = 10;
            for (Command cmd : Command.commands) {
                if (Command.commands.indexOf(cmd) < (lineLimit / 2)*pageNum)
                    continue;

                String thing = " ";

                if (!first)
                    thing = "\n ";
                else
                    first = false;

                MutableText cmdName = (MutableText) Text.of(cmd.CommandName());
                cmdName.setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$help "+cmd.CommandName())));

                var before = out.copy();
                out.append(thing);
                out.append(cmdName);
                out.append("§r§6§o " + cmd.GetArgStr() + "§r\n §7 " + cmd.ShortHelpMessage());

                if (out.getString().split("\n").length > lineLimit) {
                    out = before;
                    break;
                }

                if (Command.commands.indexOf(cmd) + 1 >= Command.commands.size())
                    reachedEnd = true;
            }

            if (previousList != null) {
                QuickChat.Clear(previousList);
            }

            var prev = new QuickChat.Msg("§e<-", new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$help "+(pageNum-1)));
            if (pageNum == 0)
                prev = new QuickChat.Msg("  ");

            var next = new QuickChat.Msg("§e->", new ClickEvent(ClickEvent.Action.RUN_COMMAND, "$help "+(pageNum+1)));
            if (reachedEnd)
                next = new QuickChat.Msg("  ");

            previousList = QuickChat.ShowChat(new QuickChat.Msg[]{
                new QuickChat.Msg("§6[ §eCommand List §6]\n"),
                new QuickChat.Msg(out),
                new QuickChat.Msg("\n§6[ "),
                prev,
                new QuickChat.Msg("§6 Page "+pageNum+" "),
                next,
                new QuickChat.Msg("§6 ]"),
            });
        };

        listAll.AddCallback(callback);
        page.AddCallback(callback);


        argTree = new EmptyArg[] {
            listAll, gaveName, page
        };
    }

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
    public EmptyArg[] ArgumentTree() {
        LazyInit();
        return argTree;
    }

}
