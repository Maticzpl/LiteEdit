package maticzpl.mixin.client;

import maticzpl.commands.parsing.Command;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = Screen.class, priority = 800)
public class LiteEditClickableTextMixin {
	@Inject(
		at = @At(
			value = "INVOKE",
			target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V",
			ordinal = 1
		),
		method = "handleTextClick(Lnet/minecraft/text/Style;)Z",
		cancellable = true,
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void injected(Style style, CallbackInfoReturnable<Boolean> cir, ClickEvent clickEvent, String string2) {
		if (string2.startsWith("$")) {
			Command.Call(string2.substring(1));
			cir.setReturnValue(true);
		}
	}
}