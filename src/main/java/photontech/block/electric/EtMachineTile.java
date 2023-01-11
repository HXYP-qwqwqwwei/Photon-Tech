package photontech.block.electric;

import net.minecraft.tileentity.TileEntityType;
import photontech.utils.helper.MutableDouble;
import photontech.utils.tileentity.MachineTile;

public abstract class EtMachineTile extends MachineTile {

    public EtMachineTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    // 上个游戏刻的电荷量
    private final MutableDouble lastQ = new MutableDouble(0);
    // 两个游戏刻直接电荷的变化量
    private final MutableDouble dQ = new MutableDouble(0);

    public double getI() {
        return this.dQ.value / 0.05;
    }

}
