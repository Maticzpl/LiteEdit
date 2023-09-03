package maticzpl.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Random;

public class QuickChat {
    public static class Msg {
        MutableText text;

        public Msg(String str) {
            text = (MutableText)Text.of(str);
        }
        public Msg(Text txt) {
            text = (MutableText) txt;
        }

        public Msg(String str, ClickEvent click) {
            text = (MutableText)Text.of(str);
            text.setStyle(Style.EMPTY.withClickEvent(click));
        }
    }

    public static void Clear(MessageSignatureData msg) {
        if (msg == null)
            return;
        var hud = MinecraftClient.getInstance().inGameHud.getChatHud();
        ((RemoveClientMessage) hud).liteedit_1_20_1$removeClientMessage(msg);
    }

    public static MessageSignatureData ShowChat(Text msg) {
        var bytes = new byte[256];
        new Random().nextBytes(bytes);

        var singnature = new MessageSignatureData(bytes);

        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(msg, singnature, MessageIndicator.system());
        return singnature;
    }

    public static MessageSignatureData ShowChat(Msg msg) {
        return ShowChat(msg.text);
    }

    public static MessageSignatureData ShowChat(Msg[] msgs) {
        MutableText res = (MutableText) Text.of("");
        for (var msg : msgs) {
            res.append(msg.text);
        }
        return ShowChat(res);
    }
}
