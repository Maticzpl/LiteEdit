package maticzpl.mixin.client;

import maticzpl.utils.RemoveClientMessage;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(ChatHud.class)
public class ChatHudMixin implements RemoveClientMessage {
    @Shadow @Final private List<ChatHudLine> messages;

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @Shadow @Final private static Logger LOGGER;

    @Unique
    public void liteedit_1_20_1$removeClientMessage(MessageSignatureData signature) {
        for(var msg : this.messages) {
            if(msg.signature() == null)
                continue;

            if(msg.signature().equals(signature)) {
                for(int i = 0; i < this.visibleMessages.size(); i++) {
                    var visible = this.visibleMessages.get(i);
                    // sketchy
                    if (visible.addedTime() == msg.creationTick()) {
                        this.visibleMessages.remove(visible);
                        i = -1; // removing can make the loop miss stuff
                    }
                }

                this.messages.remove(msg);
                break;
            }
        }
    }
}
