package maticzpl.commands;

import maticzpl.Miner;
import maticzpl.constraints.BlockConstraint;
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
        return "Set block type filter based on either the block held in right hand, all blocks in hotbar, any blocks in the inventory or disables the filter.";
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
            Miner.MiningBlocksConstraint.filter = BlockConstraint.FilterMode.Hand;
        }
        else if (option.equals("hotbar")) {
            Miner.MiningBlocksConstraint.filter = BlockConstraint.FilterMode.Hotbar;
        }
        else if (option.equals("allinv")) {
            Miner.MiningBlocksConstraint.filter = BlockConstraint.FilterMode.Inventory;
        }
        else if (option.equals("disabled")) {
            Miner.MiningBlocksConstraint.filter = BlockConstraint.FilterMode.Disabled;
        }
        else {
            var txt = Text.of("§cNo such filter");
            QuickChat.ShowChat(txt);
            return;
        }

        QuickChat.ShowChat(Text.of("§a" + Miner.MiningBlocksConstraint.toString()));
    }
}
