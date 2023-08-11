package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class OnHead implements Command {
    EmptyArg[] argTree;

    public OnHead() {
        var nothing = new EmptyArg(EmptyArg.End);

        nothing.AddCallback(data -> {
            var client = MinecraftClient.getInstance();
            client.interactionManager.clickCreativeStack(client.player.getMainHandStack(), 5);
        });

        argTree = new EmptyArg[] {
            nothing
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Puts item in hand onto head (:";
    }

    @Override
    public String HelpMessage() {
        return "Puts item in hand onto head (:";
    }

    @Override
    public String CommandName() {
        return "onhead";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
