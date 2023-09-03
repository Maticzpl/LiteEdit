package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.AnyStrArg;
import maticzpl.commands.parsing.arguments.BlockArg;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.commands.parsing.arguments.StrArg;
import maticzpl.jobs.BuildArea;
import maticzpl.jobs.MineArea;
import maticzpl.utils.QuickChat;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.awt.*;
import java.util.ArrayList;


public class Fill implements Command {
    EmptyArg[] argTree;

    public Fill() {
        var blockType = new BlockArg("block", EmptyArg.End);

        blockType.AddCallback(data -> {
            if (Builder.selection.areaLimit == null) {
                QuickChat.ShowChat(Text.of("§cNeed to select area to be filled"));
                return;
            }

            var block = (Block)data.pop();

            var dontMine = new ArrayList<Block>();
            dontMine.add(block);

            Builder.jobs.add(new MineArea(Builder.selection.areaLimit, dontMine, new Color(0,0,0,0)));
            Builder.jobs.add(new BuildArea(Builder.selection.areaLimit, block, Color.GREEN));

            QuickChat.ShowChat(Text.of("§a§o"+ block.getName().getString() + "§r§a designated to fill the area"));
        });

        argTree = new EmptyArg[] {
            blockType
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Designates selected area to be filled";
    }

    @Override
    public String HelpMessage() {
        return "Designates selected area to be filled with the specified block";
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
