package maticzpl.commands;

import maticzpl.Miner;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.constraints.BlockConstraint;
import maticzpl.utils.QuickChat;
import net.minecraft.text.Text;

public class BlockFilter implements Command {
    protected EmptyArg[] argTree;

    public BlockFilter() {
        var hand = new StrArg("hand", EmptyArg.End);
        var hotbar = new StrArg("hotbar", EmptyArg.End);
        var all = new StrArg("allinv", EmptyArg.End);
        var dis = new StrArg("disabled", EmptyArg.End);

        hand.AddCallback(data -> {
            Miner.MiningBlocksConstraint.filter = BlockConstraint.FilterMode.Hand;
            QuickChat.ShowChat(Text.of("§a" + Miner.MiningBlocksConstraint.toString()));
        });
        hotbar.AddCallback(data -> {
            Miner.MiningBlocksConstraint.filter = BlockConstraint.FilterMode.Hotbar;
            QuickChat.ShowChat(Text.of("§a" + Miner.MiningBlocksConstraint.toString()));
        });
        all.AddCallback(data -> {
            Miner.MiningBlocksConstraint.filter = BlockConstraint.FilterMode.Inventory;
            QuickChat.ShowChat(Text.of("§a" + Miner.MiningBlocksConstraint.toString()));
        });
        dis.AddCallback(data -> {
            Miner.MiningBlocksConstraint.filter = BlockConstraint.FilterMode.Disabled;
            QuickChat.ShowChat(Text.of("§a" + Miner.MiningBlocksConstraint.toString()));
        });

        argTree = new EmptyArg[] {
            hand, hotbar, all, dis
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
    public String Arguments() {
        return "'hand' | 'hotbar' | 'allinv' | 'disabled'";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
