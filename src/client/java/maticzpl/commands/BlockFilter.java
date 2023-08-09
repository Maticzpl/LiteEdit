package maticzpl.commands;

import maticzpl.Miner;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.constraints.BlockConstraint;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class BlockFilter implements Command {
    protected EmptyArg[] argTree;

    public BlockFilter() {
        var allowed = new ArrayList<String>();
        allowed.add("hand");
        allowed.add("hotbar");
        allowed.add("allinv");
        allowed.add("disabled");

        var setting = new StrArg(allowed, "", EmptyArg.End);

        setting.AddCallback(data -> {
            var bc = Miner.MiningBlocksConstraint;
            switch ((String) data.pop()) {
                case "hand" -> bc.filter = BlockConstraint.FilterMode.Hand;
                case "hotbar" -> bc.filter = BlockConstraint.FilterMode.Hotbar;
                case "allinv" -> bc.filter = BlockConstraint.FilterMode.Inventory;
                case "disabled" -> bc.filter = BlockConstraint.FilterMode.Disabled;
            }

            QuickChat.ShowChat(Text.of("§a" + bc.toString()));
        });

        argTree = new EmptyArg[] {
                setting
        };
    }

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
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
