package maticzpl.commands;

import maticzpl.Miner;
import maticzpl.utils.QuickChat;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BlockFilter implements Command {
    @Override
    public String ShortHelpMessage() {
        return "Set which blocks in your inventory to mine";
    }

    @Override
    public String HelpMessage() {
        return "Set which blocks in your inventory to mine"; // Todo longer one
    }

    @Override
    public String CommandName() {
        return "blockfilter";
    }

    @Override
    public String Arguments() {
        return "'hand' | 'hotbar' | 'allinv' | 'disabled'";
    }

    @Override
    public void Execute(CommandArguments arguments) {
        var option = arguments.NextStr().unwrap();
        arguments.ExpectEnd();
        if (option.equals("hand")) {
            Miner.filter = Miner.FilterMode.Hand;
            QuickChat.ShowChat(Text.of("Only blocks in right hand will be mined"));
        }
        else if (option.equals("hotbar")) {
            Miner.filter = Miner.FilterMode.Hotbar;
            QuickChat.ShowChat(Text.of("Only blocks in hotbar will be mined"));
        }
        else if (option.equals("allinv")) {
            Miner.filter = Miner.FilterMode.Inventory;
            QuickChat.ShowChat(Text.of("Only blocks in inventory will be mined"));
        }
        else if (option.equals("disabled")) {
            Miner.filter = Miner.FilterMode.Disabled;
            QuickChat.ShowChat(Text.of("All blocks will be mined"));
        }
        else {
            MutableText txt = (MutableText) Text.of("No such filter");
            txt.formatted(Formatting.RED);
            QuickChat.ShowChat(txt);
        }
    }
}
