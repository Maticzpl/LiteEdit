package maticzpl.commands.parsing;

import maticzpl.commands.*;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

import java.util.*;

public interface Command {
    ArrayList<Command> commands = new ArrayList<>();
    Map<String, String> aliases = Map.ofEntries(
        Map.entry("f", "fill"),
        Map.entry("mv", "select move"),
        Map.entry("ex", "extend"),
        Map.entry("rep", "repeat")
    );

    private static void LazyInit() {
        if (commands.isEmpty()) {
            commands.add(new Help());
            commands.add(new WandCmd());
            commands.add(new Select());
            commands.add(new Extend());
            commands.add(new Fill());
            commands.add(new Mine());
            commands.add(new Drain());
            commands.add(new Smooth());
            commands.add(new Jobs());
            commands.add(new QuickMenu());
            commands.add(new SeeThrough());
            commands.add(new Platform());
            commands.add(new Repeat());
            commands.add(new OnHead());
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

    public static Optional<String[]> SplitWithParenthesis(String cmd, char separator, boolean keepParenthesis) {
        ArrayList<String> split = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenthesisDepth = 0;

        for (char c : cmd.toCharArray()) {
            if(c == '(') {
                parenthesisDepth++;
                if (parenthesisDepth - 1 == 0 && !keepParenthesis)
                    continue;
            }
            if(c == ')') {
                parenthesisDepth--;
                if (parenthesisDepth == 0 && !keepParenthesis)
                    continue;
            }
            if (c == separator && parenthesisDepth == 0) {
                split.add(current.toString());
                current = new StringBuilder();
                continue;
            }

            current.append(c);
        }
        if (!current.isEmpty())
            split.add(current.toString());

        if (parenthesisDepth != 0)
            return Optional.empty();

        return Optional.of(split.toArray(new String[0]));
    }

    static void Call(String cmdString) {
        LazyInit();

        var argsOpt = SplitWithParenthesis(cmdString, ' ', false);

        if (argsOpt.isEmpty()) {
            QuickChat.ShowChat(Text.of("§c§lERROR: §r§cBad parenthesis"));
            return;
        }
        var args = argsOpt.get();

        if (aliases.containsKey(args[0])) {
            Call(cmdString.replace(args[0], aliases.get(args[0])));
            return;
        }

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
                if (!Arrays.equals(arg.ParseAfter, EmptyArg.End) || arg.Displayed().isEmpty()) {
                    allCanEnd = false;
                }
            }
            boolean skippable = false;
            for (var arg : v) {
                if (Arrays.equals(arg.ParseAfter, EmptyArg.End) && !allCanEnd) {
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
