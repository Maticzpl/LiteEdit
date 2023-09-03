package maticzpl.commands.parsing.arguments;

import maticzpl.Builder;
import maticzpl.utils.QuickChat;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.ArrayList;

public class BlockArg extends AnyStrArg {

    public BlockArg(String name, EmptyArg[] DoAfter) {
        super(name, DoAfter);
    }

    @Override
    public boolean IsValid(String argStr) {
        if (!super.IsValid(argStr))
            return false;

        var ident = Identifier.tryParse(argStr);
        if (ident == null)
            return false;

        return Registries.BLOCK.getOrEmpty(ident).isPresent();
    }

    @Override
    public Object ParseValue(String val) {
        return Registries.BLOCK.get(new Identifier(val));
    }

    @Override
    public String Expected() {
        return "Block";
    }

    public BlockArg[] arr() {
        return new BlockArg[] {this};
    }
}
