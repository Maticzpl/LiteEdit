package maticzpl.commands.parsing;

import maticzpl.commands.*;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public interface Command {
    public static ArrayList<Command> commands = new ArrayList<>();

    private static void LazyInit() {
        if (commands.isEmpty()) {
            commands.add(new Help());
            commands.add(new WandCmd());
            commands.add(new Select());
            commands.add(new Extend());
            commands.add(new BlockFilter());
            commands.add(new Fill());
            commands.add(new SeeThrough());
            commands.add(new Platform());
            commands.add(new OnHead());
            commands.add(new QuickMenu());
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
                var val = arg.ParseValue(currentArg);
                if (val != null)
                    data.push(val);
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

        boolean found = false;
        for (Command cmd : commands) {
            if (args[0].equals(cmd.CommandName())) {
                found = true;

                var data = new Stack<Object>();
                if(ParseArgsRecursive(args, 1, data, cmd.ArgumentTree()) < args.length) {
                    QuickChat.ShowChat(Text.of("§6§lWarning: §r§6Too many arguments"));
                }
            }
        }

        if (!found) {
            QuickChat.ShowChat(Text.of("§c§lERROR: §r§cCommand not found. Use $help to list all commands"));
        }
    }

    // this is a fucking mess
    static String ArgStrRecursive(EmptyArg[] tree) {
        // Java (:
        final StringBuilder[] out = {new StringBuilder()};

        var branches = new HashMap<EmptyArg[], ArrayList<EmptyArg>>();
        for (var branch : tree) {
            if (!branches.containsKey(branch.ParseAfter))
                branches.put(branch.ParseAfter, new ArrayList<>());

            branches.get(branch.ParseAfter).add(branch);
        }

        // WHY JAVA
        final boolean[] firstBranch = {true};
        branches.forEach((k, v) -> {
            var sb = out[0];

            if (!firstBranch[0])
                sb.append("| ");

            firstBranch[0] = false;

            // This only works for depth difference up to 1 >_>
            // I'm too exhausted to make it better rn
            boolean allCanEnd = true;
            for (var arg : v) {
                if (!arg.ParseAfter.equals(EmptyArg.End) || arg.Displayed().isEmpty()) {
                    allCanEnd = false;
                }
            }
            boolean skippable = false;
            for (var arg : v) {
                if (arg.ParseAfter.equals(EmptyArg.End) && !allCanEnd) {
                    skippable = true;
                    break;
                }

                for (var option : arg.ParseAfter) {
                    if (v.contains(option)) {
                        skippable = true;
                        break;
                    }
                }
            }

            if (!(v.size() == 1 && v.get(0).Displayed().isEmpty())) {
                if (skippable)
                    sb.append("[");
                else
                    sb.append("<");

                boolean first = true;
                for (var arg : v) {
                    if (!first)
                        sb.append(" | ");

                    if (!arg.Displayed().isEmpty()) {
                        sb.append(arg.Displayed());
                        first = false;
                    }
                }

                if (skippable)
                    sb.append("] ");
                else
                    sb.append("> ");
            }

            if (k.length > 0)
                sb.append(ArgStrRecursive(k));
        });
        return out[0].toString();
    }

    default String GetArgStr() {
        var tree = ArgumentTree();

        return ArgStrRecursive(tree);
    }

    String ShortHelpMessage();
    String HelpMessage();
    String CommandName();
    EmptyArg[] ArgumentTree();
}
