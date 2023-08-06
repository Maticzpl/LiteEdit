package maticzpl;

import maticzpl.commands.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

public class AutoMinerClient implements ClientModInitializer {
	private Miner miner;

	@Override
	public void onInitializeClient() {
		miner = new Miner();

		ClientSendMessageEvents.ALLOW_CHAT.register(chat -> {
			if (chat.startsWith("$")) {
				Command.Call(chat.substring(1));
				return false;
			}
			if (chat.toLowerCase().startsWith("t$")) { // anti typo :D
				Command.Call(chat.substring(2));
				return false;
			}

			return true;
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			miner.Mine(client);
		});
	}
}