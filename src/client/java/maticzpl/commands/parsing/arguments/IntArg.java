package maticzpl.commands.parsing.arguments;

public class IntArg extends AnyStrArg {

    public IntArg(EmptyArg[] DoAfter) {
        super(DoAfter);
    }
    public IntArg(EmptyArg DoAfter) {
        super(DoAfter);
    }

    @Override
    public boolean IsValid(String argStr) {
        boolean ok = true;

        try {
            int ignored = Integer.parseInt(argStr);
        }
        catch (NumberFormatException e) {
            ok = false;
        }

        return super.IsValid(argStr) && ok;
    }

    @Override
    public Object ParseValue(String val) {
        return Integer.parseInt(val);
    }

    @Override
    public String Expected() {
        return "Integer";
    }
}
