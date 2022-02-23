package photontech.utils.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import photontech.init.PtCapabilities;
import photontech.utils.capability.electric.IEtCapacitor;

import javax.annotation.Nullable;

public interface IChargeExchange {

    /**
     * 和附近的TE进行电荷交换
     * @param from 自身用于交换的电容器
     * @param otherTE 其他TE
     * @param side 相对于otherTE的方位
     */
    default void chargeExchangeWithTile(IEtCapacitor from, @Nullable TileEntity otherTE, Direction side) {
        if (otherTE == null) return;
        otherTE.getCapability(PtCapabilities.CONDUCTOR, side).ifPresent(to -> IEtCapacitor.chargeExchange(from, to));
    }
}
