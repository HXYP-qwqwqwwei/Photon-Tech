package photontech.block.electric.infiniteBattery;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.capability.electric.IMutableConductor;
import photontech.utils.capability.electric.IPtCapacitor;
import photontech.utils.capability.electric.InfiniteCapacitor;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PtInfiniteBatteryTile extends PtMachineTile {

    protected LazyOptional<IMutableConductor> positive = LazyOptional.of(() -> InfiniteCapacitor.create(10.0 / 2));
    protected LazyOptional<IMutableConductor> negative = LazyOptional.of(() -> InfiniteCapacitor.create(-10.0 / 2));

    public PtInfiniteBatteryTile(double voltage) {
        super(PtTileEntities.INFINITE_BATTERY.get());
//        this.positive = LazyOptional.of(() -> InfiniteCapacitor.create(voltage / 2));
//        this.negative = LazyOptional.of(() -> InfiniteCapacitor.create(-voltage / 2));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        if (side == facing) {
            return this.positive.cast();
        }
        else if (side == facing.getOpposite()) {
            return this.negative.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            Direction direction = this.getBlockState().getValue(BlockStateProperties.FACING);
            this.positive.ifPresent(self -> {
//                LogManager.getLogger().info("self U=" + self.getU());
                TileEntity tileEntity = this.level.getBlockEntity(this.worldPosition.relative(direction));
//                LogManager.getLogger().info(tileEntity);
                if (tileEntity != null) {
                    tileEntity.getCapability(PtCapabilities.CONDUCTOR, direction.getOpposite()).ifPresent(other -> {
//                        LogManager.getLogger().info("other U=" + other.getU());
                        IPtCapacitor.chargeExchange(self.get(), other.get());
                    });
                }
            });
            this.negative.ifPresent(self -> {
                TileEntity tileEntity = this.level.getBlockEntity(this.worldPosition.relative(direction.getOpposite()));
                if (tileEntity != null) {
                    tileEntity.getCapability(PtCapabilities.CONDUCTOR, direction).ifPresent(other -> {
                        IPtCapacitor.chargeExchange(self.get(), other.get());
                    });
                }
            });
        }

    }
}
