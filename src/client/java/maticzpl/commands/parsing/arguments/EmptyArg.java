package maticzpl.commands.parsing.arguments;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

public class EmptyArg {
    public static EmptyArg[] End = new EmptyArg[]{};

    public EmptyArg[] ParseAfter;
    public ArrayList<Consumer<Stack<Object>>> Callbacks = new ArrayList<>();

    public EmptyArg(EmptyArg[] DoAfter) {
        ParseAfter = DoAfter;
    }
    public EmptyArg(EmptyArg DoAfter) {
        ParseAfter = new EmptyArg[] {DoAfter};
    }

    public boolean IsValid(String argStr) {
        return argStr == null || argStr.trim().isEmpty();
    }

    public Object ParseValue(String val) {
        return null;
    }

    public void AddCallback(Consumer<Stack<Object>> callback) {
        Callbacks.add(callback);
    }

    public void DoCallbacks(Stack<Object> dataStack) {
        for(var callback : Callbacks) {
            callback.accept(dataStack);
        }
    }

    public String Expected() {
        return "Nothing";
    }
}
