package maticzpl.commands;

import java.util.ArrayList;

public interface Command {
    public static ArrayList<Command> commands = new ArrayList<>();

    private static void LazyInit() {
        if (commands.isEmpty()) {
            commands.add(new Help());
            commands.add(new SetArea());
            commands.add(new BlockFilter());
            commands.add(new WandCmd());
            commands.add(new SeeThrough());
        }
    }

    static void Call(String cmdString) {
        LazyInit();

        var args = cmdString.split(" ");

        for (Command cmd: commands) {
            if (args[0].equals(cmd.CommandName())) {
                try {
                    cmd.Execute(new CommandArguments(args, cmd.Arguments()));
                }
                catch (RuntimeException ignored) {}
            }
        }
    }

    String ShortHelpMessage();
    String HelpMessage();
    String CommandName();
    String Arguments(); // no spaces between <> and [] pls
    void Execute(CommandArguments arguments);
}
