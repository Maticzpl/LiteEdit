package maticzpl.commands;

import maticzpl.LiteEditClient;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

public class SeeThrough implements Command {
    protected EmptyArg[] argTree;

    public SeeThrough() {
        var toggle = new EmptyArg(EmptyArg.End);

        toggle.AddCallback(data -> {
            LiteEditClient.renderer.throughWalls = !LiteEditClient.renderer.throughWalls;
            if (LiteEditClient.renderer.throughWalls)
                QuickChat.ShowChat(Text.of("§aArea outline will be visible through walls"));
            else
                QuickChat.ShowChat(Text.of("§aArea outline will not be visible through walls"));
        });

        argTree = new EmptyArg[] {
            toggle
        };
    }

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
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
