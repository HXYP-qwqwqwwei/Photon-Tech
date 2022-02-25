package photontech.event.block_event;

import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import photontech.block.electric.wire.PtWireTile;
import photontech.utils.capability.electric.EtTransmissionLine;
import photontech.world_data.EtTransmissionLineData;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EtWireBlockEvent {
    @SubscribeEvent
    public static void onWirePlaceEvent(BlockEvent.EntityPlaceEvent event) {
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        BlockState blockState = event.getState();
        TileEntity selfTile = level.getBlockEntity(selfPos);
        if (selfTile instanceof PtWireTile) {
            PtWireTile wire = (PtWireTile) selfTile;

            PtWireTile nearbyOldestWireTile = null;
            for (Direction side : Direction.values()) {
                if (blockState.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(side))) {
                    TileEntity nearbyTile = level.getBlockEntity(selfPos.relative(side));
                    if (nearbyTile instanceof PtWireTile) {
                        if (nearbyOldestWireTile == null || ((PtWireTile) nearbyTile).getId() > nearbyOldestWireTile.getId()) {
                            nearbyOldestWireTile = (PtWireTile) nearbyTile;
                        }
                    }
                }
            }

            EtTransmissionLineData data = EtTransmissionLineData.get(level);
            if (nearbyOldestWireTile != null) {
                wire.setId(nearbyOldestWireTile.getId());
                data.put(wire.getId(), null);
            }
            else {
                int id = data.getNextID();
                wire.setId(id);
                data.put(id, EtTransmissionLine.create(wire.capacity, wire.overloadEtCurrent));
            }
        }
    }

    @SubscribeEvent
    public static void onWireRemoveEvent(BlockEvent.BreakEvent event) {
        BlockPos selfPos = event.getPos();
        IWorld level = event.getWorld();
        BlockState blockState = event.getState();
        TileEntity selfTile = level.getBlockEntity(selfPos);
        EtTransmissionLineData data = EtTransmissionLineData.get(level);
        if (selfTile instanceof PtWireTile) {

            for (Direction side : Direction.values()) {
                if (blockState.getValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(side))) {
                    TileEntity nearbyTile = level.getBlockEntity(selfPos.relative(side));
                    if (nearbyTile instanceof PtWireTile) {
                        PtWireTile nearbyWire = (PtWireTile) nearbyTile;
                        int newId = EtTransmissionLineData.get(level).getNextID();
                        data.remove(nearbyWire.getId());
                        data.put(newId, EtTransmissionLine.create(nearbyWire.capacity, nearbyWire.overloadEtCurrent));
                        nearbyWire.setId(newId);
                    }
                }
            }

        }
    }
}
