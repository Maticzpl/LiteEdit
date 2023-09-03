package maticzpl.commands;

import maticzpl.Builder;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class Platform implements Command {
    EmptyArg[] argTree;

    public Platform() {
        var nothing = new EmptyArg(EmptyArg.End);

        nothing.AddCallback(data -> {
            var client = MinecraftClient.getInstance();
            var pos = client.player.getBlockPos().add(0,-1,0);

            Builder.PlaceBlock(Blocks.GLASS, pos);
        });

        argTree = new EmptyArg[] {
            nothing
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Places a glass block below you in air";
    }

    @Override
    public String HelpMessage() {
        return "Places a glass block below you in air";
    }

    @Override
    public String CommandName() {
        return "platform";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
