package maticzpl.commands.parsing.arguments;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Function;

public class AnyStrArg extends EmptyArg {
    public AnyStrArg(EmptyArg[] DoAfter) {
        super(DoAfter);
    }
    public AnyStrArg(EmptyArg DoAfter) {
        super(DoAfter);
    }

    public boolean IsValid(String argStr) {
        return argStr != null && (!argStr.trim().isEmpty());
    }

    public Object ParseValue(String val) {
        return val;
    }

    public String Expected() {
        return "String";
    }
}
