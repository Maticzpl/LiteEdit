package maticzpl;

import maticzpl.commands.parsing.Command;
import maticzpl.rendering.AreaRenderer;
import maticzpl.utils.QuickChat;
import me.x150.renderer.event.RenderEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class LiteEditClient implements ClientModInitializer {
	public static Wand wand = new Wand();
	public static Builder miner;
	public static AreaRenderer renderer;

	@Override
	public void onInitializeClient() {
		miner = new Builder();
		renderer = new AreaRenderer();
		wand = new Wand();

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

			return true;
		});

		RenderEvents.WORLD.register(matrixStack -> {
			renderer.DrawBounds(matrixStack);
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			miner.Mine(client);
			if (wand.cooldown > 0)
				wand.cooldown--;
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {
			var client = MinecraftClient.getInstance();
			if (player.equals(client.player))
			{
				if(wand.DoWand()) {
					return new TypedActionResult<>(ActionResult.FAIL, null);
				}
			}
            return new TypedActionResult<>(ActionResult.PASS, null);
        });
	}
}