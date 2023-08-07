package maticzpl.commands;

import maticzpl.Miner;
import maticzpl.utils.QuickChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class SetArea implements Command {
    @Override
    public String ShortHelpMessage() {
        return "Limits the mining area";
    }

    @Override
    public String HelpMessage() {
        return "If given coordinates, prevents the autominer from breaking blocks outside of the specified area. If given none, the area limit will be removed.";
    }

    @Override
    public String CommandName() {
        return "setarea";
    }

    @Override
    public String Arguments() {
        return "<x> <y> <z> <x1> <y1> <z1> | 'none'";
    }

    @Override
    public void Execute(CommandArguments args) {
        if (!args.IsNextInt()) {
            if (!args.ExpectNextStr("none"))
                return;

            args.ExpectEnd();

            Miner.MiningAreaConstraint.areaLimit = null;
            QuickChat.ShowChat(Text.of("§aArea limit cleared"));
        }
        else {
            int x = args.NextInt().unwrap();
            int y = args.NextInt().unwrap();
            int z = args.NextInt().unwrap();
            BlockPos first = new BlockPos(x, y, z);

            int x1 = args.NextInt().unwrap();
            int y1 = args.NextInt().unwrap();
            int z1 = args.NextInt().unwrap();
            BlockPos second = new BlockPos(x1, y1, z1);
            args.ExpectEnd();

            Miner.MiningAreaConstraint.areaLimit = new Pair<>(first, second);

            var size = Miner.MiningAreaConstraint.GetSize();

            StringBuilder str = new StringBuilder("§aArea limit set (");
            str.append(size.x).append("x")
                .append(size.y).append("x")
                .append(size.z).append(")");

            QuickChat.ShowChat(Text.of(String.valueOf(str)));
        }
    }
}
