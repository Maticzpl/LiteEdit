package maticzpl.commands;

import maticzpl.Miner;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

public class SeeThrough implements Command {

    @Override
    public String ShortHelpMessage() {
        return "Toggles if the area limit outline is visible through walls";
    }

    @Override
    public String HelpMessage() {
        return "Toggles if the area limit outline is visible through walls";
    }

    @Override
    public String CommandName() {
        return "seethrough";
    }

    @Override
    public String Arguments() {
        return "";
    }

    @Override
    public void Execute(CommandArguments arguments) {
        arguments.ExpectEnd();
        Miner.MiningAreaConstraint.throughWalls = !Miner.MiningAreaConstraint.throughWalls;
        if (Miner.MiningAreaConstraint.throughWalls)
            QuickChat.ShowChat(Text.of("§aArea outline will be visible through walls"));
        else
            QuickChat.ShowChat(Text.of("§aArea outline will not be visible through walls"));
    }
}
