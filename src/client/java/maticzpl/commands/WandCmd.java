package maticzpl.commands;

import maticzpl.LiteEditClient;
import maticzpl.commands.parsing.Command;
import maticzpl.commands.parsing.arguments.EmptyArg;
import maticzpl.utils.QuickChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

public class WandCmd implements Command {
    protected EmptyArg[] argTree;

    public WandCmd() {
        var set = new EmptyArg(EmptyArg.End);

        set.AddCallback(data -> {
            LiteEditClient.wand.WandItem = MinecraftClient.getInstance().player.getInventory().getMainHandStack().getRegistryEntry().value();

            if (LiteEditClient.wand.WandItem != Item.byRawId(0)) {
                QuickChat.ShowChat(Text.of("§aWand assigned to "+ LiteEditClient.wand.WandItem.getName().getString()));
            }
            else {
                LiteEditClient.wand.WandItem = null;
                QuickChat.ShowChat(Text.of("§aWand not assigned to any item"));
            }
        });

        argTree = new EmptyArg[] {
            set
        };
    }

    @Override
    public String ShortHelpMessage() {
        return "Makes current item in hand the wand. Right click with wand to select area";
    }

    @Override
    public String HelpMessage() {
        return "Makes current item in hand the wand which can select an area by right clicking 2 corners";
    }

    @Override
    public String CommandName() {
        return "wand";
    }

    @Override
    public EmptyArg[] ArgumentTree() {
        return argTree;
    }
}
