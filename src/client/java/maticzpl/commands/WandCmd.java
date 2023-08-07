package maticzpl.commands;

import maticzpl.AutoMinerClient;
import maticzpl.utils.QuickChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class WandCmd implements Command {
    @Override
    public String ShortHelpMessage() {
        return "Makes current item in hand the wand like in WorldEdit";
    }

    @Override
    public String HelpMessage() {
        return "Makes current item in hand the wand which can set area corners";
    }

    @Override
    public String CommandName() {
        return "wand";
    }

    @Override
    public String Arguments() {
        return "";
    }

    @Override
    public void Execute(CommandArguments arguments) {
        if (AutoMinerClient.wand.WandItem == null) {
            AutoMinerClient.wand.WandItem = MinecraftClient.getInstance().player.getInventory().getMainHandStack().getRegistryEntry().value();
            QuickChat.ShowChat(Text.of("§aWand assigned to "+AutoMinerClient.wand.WandItem.getName().getString()));
        }
        else {
            AutoMinerClient.wand = null;
            QuickChat.ShowChat(Text.of("§aWand not assigned to any item"));
        }
    }
}
