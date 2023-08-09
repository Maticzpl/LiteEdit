package maticzpl.commands.parsing.arguments;

import java.util.ArrayList;

public class StrArg extends AnyStrArg {
    protected ArrayList<String> Allowed;

    public StrArg(ArrayList<String> AllowedValues, String name, EmptyArg[] DoAfter) {
        super(name, DoAfter);
        Allowed = AllowedValues;
    }
    public StrArg(String AllowedValue, String name, EmptyArg[] DoAfter) {
        super(name, DoAfter);
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

    public String Displayed() {
        if (name.isEmpty()) {
            StringBuilder str = new StringBuilder("'" + Allowed.get(0) + "'");
            for (int i = 1; i < Allowed.size(); i++) {
                str.append(" | '").append(Allowed.get(i)).append("'");
            }

            return str.toString();
        }
        return name;
    }


    public StrArg[] arr() {
        return new StrArg[] {this};
    }
}
