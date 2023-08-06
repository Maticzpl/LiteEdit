package maticzpl.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class QuickChat {
    // eh im just lazy ok?
    public static void ShowChat(Text msg) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(msg);
    }
}
