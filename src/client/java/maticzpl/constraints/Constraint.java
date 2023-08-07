package maticzpl.constraints;

import net.minecraft.util.math.BlockPos;

public interface Constraint {
    default void DoCache() {};

    boolean Allowed(BlockPos pos);
}
