package photontech.block.electric;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import photontech.init.PtCapabilities;
import photontech.utils.helper.MutableDouble;
import photontech.utils.tileentity.IChargeExchange;
import photontech.utils.tileentity.PtMachineTile;

public abstract class PtElectricMachineTile extends PtMachineTile implements IChargeExchange {

    public PtElectricMachineTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    // 上个游戏刻的电荷量
    private final MutableDouble lastQ = new MutableDouble(0);
    // 两个游戏刻直接电荷的变化量
    private final MutableDouble dQ = new MutableDouble(0);

    /**
     * 和周围的电子器件进行电荷交换
     * @param validDirections 可能进行交换的方向
     */
    protected void chargeExchangeByDirections(Direction[] validDirections) {
        assert level != null;
        for (Direction direction : validDirections) {
            this.getCapability(PtCapabilities.CONDUCTOR, direction).ifPresent(self -> {
                // 记录电荷量和变化量
                this.dQ.value = self.getQ() - this.lastQ.value;
                this.lastQ.value = self.getQ();
                // 对所有的方向进行电荷转移
                this.chargeExchangeWithTile(self, level.getBlockEntity(this.worldPosition.relative(direction)), direction.getOpposite());
            });
        }
    }

    public double getI() {
        return this.dQ.value / 0.05;
    }

}
