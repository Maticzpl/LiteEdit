package maticzpl.commands;

import maticzpl.AutoMiner;
import maticzpl.AutoMinerClient;
import maticzpl.utils.Result;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.logging.LogManager;

public interface Command {
    public static ArrayList<Command> commands = new ArrayList<>();

    private static void LazyInit() {
        if (commands.isEmpty()) {
            commands.add(new Help());
            commands.add(new SetArea());
            commands.add(new BlockFilter());
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
