package maticzpl.commands;

import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.AnyStrArg;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.IntArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

public class Repeat implements Command {
    EmptyArg[] argTree;

    public Repeat() {
        var times = new IntArg("times", EmptyArg.End);
        var command = new AnyStrArg("commands", times.arr());

        times.AddCallback(data -> {
            var n = (int)data.pop();
            var cmds = (String)data.pop();
            var cmdListOpt = Command.SplitWithParenthesis(cmds, '$', true);

            if (cmdListOpt.isEmpty()) {
                QuickChat.ShowChat(Text.of("§c§lERROR: §r§cBad parenthesis"));
                return;
            }

            var cmdList = cmdListOpt.get();

            for (int i = 0; i < n; i++) {
                for(int c = 1; c < cmdList.length; c++) {
                    var cmd = cmdList[c];
                    Command.Call(cmd);
                }
            }

        });

        argTree = new EmptyArg[] {
            command
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Runs the commands n times";
    }

    @Override
    public String HelpMessage() {
        return "Runs the commands n times";
    }

    @Override
    public String CommandName() {
        return "repeat";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
