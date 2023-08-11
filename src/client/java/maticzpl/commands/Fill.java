package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.AnyStrArg;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.utils.QuickChat;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;


public class Fill implements Command {
    EmptyArg[] argTree;

    public Fill() {
        var blockType = new AnyStrArg("block", EmptyArg.End);
        var none = new StrArg("none", "block", EmptyArg.End);

        blockType.AddCallback(data -> {
            var name = (String)data.pop();
            Block block;
            try {
                block = Registries.BLOCK.get(new Identifier(name));

                Builder.BlockPlacingConstraint.currentBlock = Registries.BLOCK.get(new Identifier(name));
            }
            catch (InvalidIdentifierException e) {
                QuickChat.ShowChat(Text.of("§cBlock not found"));
                return;
            }

            QuickChat.ShowChat(Text.of("§a§o"+ block.getName().getString() + "§r§a will be placed in selected area"));
        });

        none.AddCallback(data -> {
            Builder.BlockPlacingConstraint.currentBlock = null;
            QuickChat.ShowChat(Text.of("§aNo blocks will be placed in selected area"));
        });

        argTree = new EmptyArg[] {
            none, blockType
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Makes LiteEdit fill the selected area with blocks";
    }

    @Override
    public String HelpMessage() {
        return "Makes LiteEdit fill the selected area with blocks";
    }

    @Override
    public String CommandName() {
        return "fill";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
