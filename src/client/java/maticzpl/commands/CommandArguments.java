package maticzpl.commands;

import maticzpl.utils.QuickChat;
import maticzpl.utils.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.ParseException;
import java.util.ArrayList;

public class CommandArguments {
    protected String[] str;
    protected ArrayList<String[]> argTemplates = new ArrayList<>();
    protected int index;

    public CommandArguments(String[] args, String argumentTemplates) {
        str = args;
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

    public void ShowError(String error) {
        var err = (MutableText)Text.of("ERROR: ");
        err.append("$");
        for (var argument : str) {
            err.append(argument);
            err.append(" ");
        }
        err.append("\n");
        err.append(error);
        err.formatted(Formatting.RED);

        QuickChat.ShowChat(err);
        throw new RuntimeException();
    }

    public Result<String> NextStr() {
        try {
            return new Result<>(str[index++]);
        } catch (IndexOutOfBoundsException e) {
            ShowError(
                "Missing argument. Expected " +
                ExpectedArg(index - 2) // 1 because already incremented, 2 because 0 is a name
            );
            return new Result<String>();
        }
    }

    public boolean IsNextEmpty() {
        return str.length <= index;
    }

    public Result<Integer> NextInt() {
        var asStr = NextStr();
        if (asStr.isSome()) {
            try {
                return new Result<>(Integer.parseInt(asStr.unwrap()));
            }
            catch (NumberFormatException e) {
                ShowError(str[index - 1] + " found, expected integer");
                return new Result<Integer>(null);
            }
        }
        return new Result<Integer>(null);
    }

    public void ExpectEnd() {
        if (str.length > index) {
            String error = "Unexpected argument \"" + str[index] + "\"";
            ShowError(error);
        }
    }

    public void Rewind(int n) {
        index -= n;
    }
}
