package maticzpl.commands.parsing.arguments;

import java.util.ArrayList;

public class StrArg extends AnyStrArg {
    protected ArrayList<String> Allowed;

    // I love combinatorial explosions (:
    public StrArg(ArrayList<String> AllowedValues, EmptyArg[] DoAfter) {
        super(DoAfter);
        Allowed = AllowedValues;
    }
    public StrArg(ArrayList<String> AllowedValues, EmptyArg DoAfter) {
        super(DoAfter);
        Allowed = AllowedValues;
    }
    public StrArg(String AllowedValue, EmptyArg[] DoAfter) {
        super(DoAfter);
        Allowed = new ArrayList<>();
        Allowed.add(AllowedValue);
    }
    public StrArg(String AllowedValue, EmptyArg DoAfter) {
        super(DoAfter);
        Allowed = new ArrayList<>();
        Allowed.add(AllowedValue);
    }

    @Override
    public boolean IsValid(String argStr) {
        return super.IsValid(argStr) && Allowed.contains(argStr);
    }

    @Override
    public String Expected() {
        StringBuilder str = new StringBuilder("'"+Allowed.get(0)+"'");
        for (int i = 1; i < Allowed.size(); i++) {
            str.append(" or '").append(Allowed.get(i)).append("'");
        }

        return str.toString();
    }
}
