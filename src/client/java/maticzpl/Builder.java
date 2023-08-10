package maticzpl;

import maticzpl.utils.QuickChat;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class Builder {
    public static void EquipItem(ItemStack stack, int slot) {
        var client = MinecraftClient.getInstance();
        client.interactionManager.clickCreativeStack(stack, 36 + slot);
    }

    public static void PlaceBlock(Block type, BlockPos pos) {
        var client = MinecraftClient.getInstance();

        assert client.player != null;
        assert client.interactionManager != null;

        var dist = pos.toCenterPos().distanceTo(client.player.getEyePos());
        if (dist > 5.0)
            return;

        // Get block item
        var slot = client.player.getInventory().selectedSlot;
        if (
            !client.player.getInventory().getMainHandStack()
                .itemMatches(type.asItem().getDefaultStack()
                .getRegistryEntry())
        ) {
            EquipItem(type.asItem().getDefaultStack(), slot);
        }

        // Place block
        Direction chosen = Direction.DOWN;
        for (Direction dir : Direction.values()) {
            Vec3d hit = pos.toCenterPos().add(Vec3d.of(dir.getVector()).multiply(0.5));

            if (client.player.getEyePos().distanceTo(hit) > dist)
                continue;

            chosen = dir;
            break;
        }

        var target = new BlockHitResult(
            pos.toCenterPos(),
            chosen,
            pos,
            false
        );

        client.interactionManager.interactBlock(
            client.player,
            client.player.getActiveHand(),
            target
        );


    }
}
