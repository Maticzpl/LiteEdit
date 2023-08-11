package maticzpl.commands.parsing.arguments;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Function;

public class AnyStrArg extends EmptyArg {
    protected String name;

    public AnyStrArg(String name, EmptyArg[] DoAfter) {
        super(DoAfter);
        this.name = name;
    }

    public boolean IsValid(String argStr) {
        return argStr != null && (!argStr.trim().isEmpty());
    }

    public Object ParseValue(String val) {
        return val;
    }

    public String Expected() {
        if (name.isEmpty())
            return "String";
        return name;
    }

    public String Displayed() {
        return name;
    }

    public AnyStrArg[] arr() {
        return new AnyStrArg[] {this};
    }
}
