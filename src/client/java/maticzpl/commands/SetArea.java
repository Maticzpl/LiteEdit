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
        return "Limits the mining area";
    }

    @Override
    public String CommandName() {
        return "setarea";
    }

    @Override
    public String Arguments() {
        return "<x> <y> <z> <x1> <y1> <z1> | 'null'";
    }

    @Override
    public void Execute(CommandArguments args) {
        if (args.NextStr().unwrap().equals("null")) {
            Miner.areaLimit = null;
            QuickChat.ShowChat(Text.of("Area limit cleared"));
        }
        else {
            args.Rewind(1);

            int x = args.NextInt().unwrap();
            int y = args.NextInt().unwrap();
            int z = args.NextInt().unwrap();
            BlockPos first = new BlockPos(x, y, z);

            int x1 = args.NextInt().unwrap();
            int y1 = args.NextInt().unwrap();
            int z1 = args.NextInt().unwrap();
            BlockPos second = new BlockPos(x1, y1, z1);
            args.ExpectEnd();

            Miner.areaLimit = new Pair<>(first, second);

            int sx = Math.abs(x - x1);
            int sy = Math.abs(y - y1);
            int sz = Math.abs(z - z1);

            StringBuilder str = new StringBuilder("Area limit set (");
            str.append(sx).append("x")
                .append(sy).append("x")
                .append(sz).append(")");

            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(String.valueOf(str)));
        }
    }
}
