package maticzpl.commands;

import maticzpl.utils.QuickChat;
import maticzpl.utils.*;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class CommandArguments {
    protected String[] argsStr;
    protected ArrayList<String[]> argTemplates = new ArrayList<>();
    protected int index;

    public CommandArguments(String[] args, String argumentTemplates) {
        argsStr = args;
        index = 1; // Skips command name
        // This is not meant to be general purpose ok?

        for(var argOption : argumentTemplates.split(" \\| ")) {
            argTemplates.add(argOption.split(" "));
        }
    }

    protected String ExpectedArg(int index) {
        StringBuilder out = new StringBuilder();
        for(var argOption: argTemplates) {
            if (argOption.length <= index) {
                continue;
            }
            if (!out.isEmpty())
                out.append(" or ");
            out.append(argOption[index]);
        }
        return String.valueOf(out);
    }

    /// underline is the argument index which to underline
    public void ShowError(String error, int underline) {
        var err = new StringBuilder("§c§lERROR: §r§c$");

        int i = 0;
        for (var argument : argsStr) {
            String underlineStr = (underline == i++) ? "§n" : "";
            err.append("§c" + underlineStr + argument + "§r ");
        }
        if (underline > i) {
            err.append("§c§n?§r");
        }

        err.append("\n§c" + error);

        QuickChat.ShowChat(Text.of(String.valueOf(err)));
        throw new RuntimeException();
    }

    public Result<String> NextStr() {
        try {
            return new Result<>(argsStr[index++]);
        } catch (IndexOutOfBoundsException e) {
            ShowError(
                "Missing argument. Expected " +
                ExpectedArg(index - 2), // 1 because already incremented, 2 because 0 is a name
                index
            );
            return new Result<String>();
        }
    }

    public boolean IsNextEmpty() {
        return argsStr.length <= index;
    }

    public Result<Integer> NextInt() {
        var asStr = NextStr();
        if (asStr.isSome()) {
            try {
                return new Result<>(Integer.parseInt(asStr.unwrap()));
            }
            catch (NumberFormatException e) {
                ShowError(argsStr[index - 1] + " found, expected integer", index - 1);
                return new Result<Integer>(null);
            }
        }
        return new Result<Integer>(null);
    }

    public void ExpectEnd() {
        if (argsStr.length > index) {
            String error = "Unexpected argument \"" + argsStr[index] + "\"";
            ShowError(error, index);
        }
    }

    public void Rewind(int n) {
        index -= n;
    }
}
