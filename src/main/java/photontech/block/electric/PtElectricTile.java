package photontech.block.electric;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtCapabilities;
import photontech.utils.capability.electric.IPtCapacitor;
import photontech.utils.helper.MutableDouble;
import photontech.utils.tileentity.PtMachineTile;

public abstract class PtElectricTile extends PtMachineTile {

    public PtElectricTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    // 上个游戏刻的电荷量
    private final MutableDouble lastQ = new MutableDouble(0);
    // 两个游戏刻直接电荷的变化量
    private final MutableDouble dQ = new MutableDouble(0);

    /**
     * 和周围的电子器件进行电荷交换
     * @param state blockState
     * @param validDirections 可能进行交换的方向
     */
    protected void exchangeCharge(BlockState state, Direction[] validDirections) {
        assert level != null;
        this.getCapability(PtCapabilities.CONDUCTOR).ifPresent(self -> {
            // 记录电荷量和变化量
            this.dQ.value = self.getQ() - this.lastQ.value;
//            LogManager.getLogger().info("U=" + self.getU());
//            LogManager.getLogger().info("dQ=" + this.dQ.value);
            this.lastQ.value = self.getQ();
            // 对所有的方向进行电荷转移
            for (Direction direction : validDirections) {
                if (state.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(direction))) {
                    TileEntity tileEntity = level.getBlockEntity(this.worldPosition.relative(direction));
                    if (tileEntity != null) {
                        tileEntity.getCapability(PtCapabilities.CONDUCTOR, direction.getOpposite()).ifPresent(other -> IPtCapacitor.chargeExchange(self.get(), other.get()));
                    }
                }
            }
        });
    }

    public double getI() {
        return this.dQ.value / 0.05;
    }

}
