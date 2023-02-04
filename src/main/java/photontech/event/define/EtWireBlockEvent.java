package photontech.event.define;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.electric.wire.WireTile;
import photontech.data.DCWireDataManager;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EtWireBlockEvent {
    @SubscribeEvent
    public static void onWirePlaceEvent(BlockEvent.EntityPlaceEvent event) {
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        BlockState blockState = event.getState();
        TileEntity te = level.getBlockEntity(selfPos);
        if (te instanceof WireTile) {
            WireTile wire = (WireTile) te;

            // 搜索编号最大（最近更新）的导线
            WireTile nearbyLatestWire = null;
            for (Direction side : Direction.values()) {
                if (blockState.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(side))) {
                    TileEntity nearbyTile = level.getBlockEntity(selfPos.relative(side));
                    if (nearbyTile instanceof WireTile) {
                        if (nearbyLatestWire == null || ((WireTile) nearbyTile).getId() > nearbyLatestWire.getId()) {
                            nearbyLatestWire = (WireTile) nearbyTile;
                        }
                    }
                }
            }

            DCWireDataManager data = DCWireDataManager.getData(level);
            if (nearbyLatestWire != null) {
                wire.setID(nearbyLatestWire.getId());
                data.put(wire.getId(), wire::createCapacitor);
            }
            else {
                int id = data.allocateID();
                wire.setID(id);
                data.put(id, wire::createCapacitor);
            }
        }
    }

    @SubscribeEvent
    public static void onWireRemoveEvent(BlockEvent.BreakEvent event) {
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        BlockState blockState = event.getState();
        TileEntity selfTile = level.getBlockEntity(selfPos);
        DCWireDataManager data = DCWireDataManager.getData(level);
        if (selfTile instanceof WireTile) {

            for (Direction side : Direction.values()) {
                if (blockState.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(side))) {
                    TileEntity nearbyTile = level.getBlockEntity(selfPos.relative(side));
                    if (nearbyTile instanceof WireTile) {
                        WireTile nearbyWire = (WireTile) nearbyTile;
                        int id = DCWireDataManager.getData(level).allocateID();
                        data.remove(nearbyWire.getId());
                        data.put(id, nearbyWire::createCapacitor);
                        nearbyWire.setID(id);
                    }
                }
            }

        }
    }
}
