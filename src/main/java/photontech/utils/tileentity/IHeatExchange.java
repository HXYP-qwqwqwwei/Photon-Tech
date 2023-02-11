package photontech.utils.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import photontech.init.PtCapabilities;
import photontech.utils.data.heat.HeatReservoir;
import photontech.utils.data.heat.PtHeatReservoir;

import javax.annotation.Nullable;

public interface IHeatExchange {

    float ENVIRONMENT_TEMPERATURE = 298.15F;
    float CAMPFIRE_TEMPERATURE = 800F;
    double CAMPFIRE_EXTINGUISH_CHANCE = 0.001;


    HeatReservoir ENVIRONMENT = new PtHeatReservoir() {
        @Override
        public int extractHeat(int maxHeat, boolean simulate) {
            return Math.max(0, maxHeat);
        }

        @Override
        public int acceptHeat(int maxHeat, boolean simulate) {
            return Math.max(0, maxHeat);
        }

        @Override
        public float getTemperature() {
            return ENVIRONMENT_TEMPERATURE;
        }

        @Override
        public float getHeatTransferRate() {
            return 0.5F;
        }

        @Override
        public float getCapacity() {
            return 1000F;
        }
    };

    HeatReservoir CAMPFIRE = new PtHeatReservoir() {
        @Override
        public int extractHeat(int maxHeat, boolean simulate) {
            return Math.max(0, maxHeat);
        }

        @Override
        public int acceptHeat(int maxHeat, boolean simulate) {
            return 0;
        }

        @Override
        public float getTemperature() {
            return CAMPFIRE_TEMPERATURE;
        }

        @Override
        public float getHeatTransferRate() {
            return 100.0F;
        }

        @Override
       public float getCapacity() {
            return 1000F;
        }

        @Override
        public int getHeat() {
            return (int) (CAMPFIRE_TEMPERATURE * getCapacity());
        }
    };

    default void heatExchangeWithEnvironment(PtHeatReservoir reservoir) {
        HeatReservoir.heatExchange(reservoir, ENVIRONMENT);
    }

    default void heatExchangeWithEnvironment(PtHeatReservoir reservoir, float rate) {
        HeatReservoir.heatExchange(reservoir, ENVIRONMENT, rate);
    }

    default void heatExchangeWithCampfireHeat(World world, BlockPos pos, PtHeatReservoir reservoir) {
        BlockPos down = pos.relative(Direction.DOWN);
        BlockState campfire = world.getBlockState(down);
        if (campfire.getBlock() == Blocks.CAMPFIRE) {
            if (campfire.getValue(BlockStateProperties.LIT)) {
                HeatReservoir.heatExchange(reservoir, CAMPFIRE);
                if (Math.random() <= CAMPFIRE_EXTINGUISH_CHANCE) {
                    world.setBlock(down, campfire.setValue(BlockStateProperties.LIT, false), 1);
                    world.sendBlockUpdated(down, campfire, campfire, Constants.BlockFlags.BLOCK_UPDATE);
                }
            }
        }
    }

    /**
     * 和附近的TileEntity之间进行热量交换
     * @param from 自身交换所使用的热容器
     * @param other 其他Tile
     * @param side 相对于other的方位
     */
    default void heatExchangeWithTile(PtHeatReservoir from, @Nullable TileEntity other, Direction side) {
        if (other == null) return;
        other.getCapability(PtCapabilities.HEAT_RESERVOIR, side).ifPresent(to -> HeatReservoir.heatExchange(from, to));
    }

}
