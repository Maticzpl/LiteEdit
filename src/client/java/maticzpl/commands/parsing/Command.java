package maticzpl.commands.parsing;

import maticzpl.commands.*;
import maticzpl.commands.parsing.arguments.AnyStrArg;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Stack;

public interface Command {
    public static ArrayList<Command> commands = new ArrayList<>();

    private static void LazyInit() {
        if (commands.isEmpty()) {
            commands.add(new SetArea());
            commands.add(new BlockFilter());
            commands.add(new WandCmd());
            commands.add(new SeeThrough());
            commands.add(new Extend());
            commands.add(new Help());
        }
    }

    static int ParseArgsRecursive(String[] args, int i, Stack<Object> data, EmptyArg[] argTree) {
        String currentArg = null;
        try {
            currentArg = args[i];
        }
        catch (IndexOutOfBoundsException ignored) {}

        StringBuilder expected = new StringBuilder("§c§lERROR: §r§c$\n");
        expected.append("§r§cGot ");
        expected.append(currentArg == null ? "Nothing" : "'"+currentArg+"'");
        expected.append(" expected ");

        boolean first = true;
        for(var arg : argTree) {
            if (first) {
                first = false;
                expected.append(arg.Expected());
            }
            else {
                expected.append(" or ").append(arg.Expected());
            }

            if (arg.IsValid(currentArg)) {
                data.push(arg.ParseValue(currentArg));
                arg.DoCallbacks(data);
                return ParseArgsRecursive(args, i+1, data, arg.ParseAfter);
            }
        }
        if (argTree.length == 0)
            return i;

        StringBuilder cmd = new StringBuilder();
        for (int j = 0; j <= args.length; j++) {
            if(j == args.length) {
                if (currentArg == null)
                    cmd.append("§n?§r§c "); // for missing arg
                continue;
            }
            cmd.append(j == i ? "§n"+args[j] : args[j]);
            cmd.append("§r§c ");
        }
        int rep = expected.indexOf("\n");
        expected.replace(rep, rep, cmd.toString());

        QuickChat.ShowChat(Text.of(expected.toString()));
        return args.length + 1; // never show too many args here
    }

    static void Call(String cmdString) {
        LazyInit();

        var args = cmdString.split(" ");

        for (Command cmd : commands) {
            if (args[0].equals(cmd.CommandName())) {
                var data = new Stack<Object>();
                if(ParseArgsRecursive(args, 1, data, cmd.ArgumentTree()) < args.length) {
                    QuickChat.ShowChat(Text.of("§6§lWarning: §r§6Too many arguments"));
                }
            }
        }
    }

    String ShortHelpMessage();
    String HelpMessage();
    String CommandName();
    String Arguments();
    EmptyArg[] ArgumentTree();
}
