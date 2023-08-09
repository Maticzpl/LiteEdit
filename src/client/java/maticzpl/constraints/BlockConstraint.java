package maticzpl.constraints;

import maticzpl.Miner;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class BlockConstraint implements Constraint {
    public enum FilterMode {
        Hand, Inventory, Hotbar, Disabled
    }

    protected ArrayList<Item> minable;
    public FilterMode filter = FilterMode.Disabled;

    @Override
    public void DoCache() {
        var client = MinecraftClient.getInstance();

        minable = new ArrayList<Item>();
        if (filter.equals(BlockConstraint.FilterMode.Hand)) {
            minable.add(client.player.getInventory().getMainHandStack().getRegistryEntry().value());
        }
        else if (filter.equals(BlockConstraint.FilterMode.Hotbar)) {
            for (int i = 0; i <= 8; i++) {
                minable.add(client.player.getInventory().getStack(i).getRegistryEntry().value());
            }
        }
    }

    @Override
    public boolean Allowed(BlockPos block) {
        var client = MinecraftClient.getInstance();

        if (filter.equals(BlockConstraint.FilterMode.Hand) || filter.equals(BlockConstraint.FilterMode.Hotbar)) {
            return minable.contains(client.world.getBlockState(block).getBlock().asItem());
        } else if (filter.equals(BlockConstraint.FilterMode.Inventory)) {
            return client.player.getInventory().contains(client.world.getBlockState(block).getBlock().asItem().getDefaultStack());
        }

        return true;
    }

    @Override
    public String toString() {
        switch (filter) {
            case Hand -> {
                return "Only blocks in right hand will be mined";
            }
            case Hotbar -> {
                return "Only blocks in hotbar will be mined";
            }
            case Inventory -> {
                return "Only blocks in inventory will be mined";
            }
            case Disabled -> {
                return "All blocks will be mined";
            }
        }
        return "No such filter"; // why java, this switch is exhaustive
    }

}
