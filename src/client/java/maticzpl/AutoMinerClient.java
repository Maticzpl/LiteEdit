package maticzpl;

import maticzpl.commands.Command;
import maticzpl.utils.QuickChat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.Text;

public class AutoMinerClient implements ClientModInitializer {
	private Miner miner;

	@Override
	public void onInitializeClient() {
		miner = new Miner();

		ClientPlayConnectionEvents.JOIN.register((network, packets, client) -> {
			QuickChat.ShowChat(Text.of(miner.toString()));
		});

		ClientSendMessageEvents.ALLOW_CHAT.register(chat -> {
			if (chat.startsWith("$")) {
				Command.Call(chat.substring(1));
				return false;
			}
			if (chat.toLowerCase().startsWith("t$")) { // anti typo :D
				Command.Call(chat.substring(2));
				return false;
			}
			if (chat.toLowerCase().startsWith("#") || chat.toLowerCase().startsWith("t#")) {
				QuickChat.ShowChat(Text.of("Correct prefix for AutoMiner is §6$§r. Your message won't be visible on chat."));
				return false;
			}

			return true;
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			miner.Mine(client);
		});
	}
}