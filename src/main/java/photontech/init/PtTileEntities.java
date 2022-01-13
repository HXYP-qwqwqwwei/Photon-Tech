package photontech.init;

import photontech.block.electric.infiniteBattery.PtInfiniteBatteryTile;
import photontech.block.electric.wire.PtWireTile;
import photontech.block.kinetic.axle.AxleTile;
import photontech.block.kinetic.gears.PtGearsTile;
import photontech.block.heater.photon.PhotonHeaterTile;
import photontech.block.heater.solid.PtBurningItemHeaterTile;
import photontech.block.crucible.PtCrucibleTileEntity;
import photontech.block.kinetic.motor.dc_brush.DCBrushTilePartA;
import photontech.block.kinetic.motor.dc_brush.DCBrushTilePartB;
import photontech.block.mirror.PtMirrorTile;
import photontech.utils.Utils;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PtTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Utils.MOD_ID);

    public static final RegistryObject<TileEntityType<PtCrucibleTileEntity>> CRUCIBLE_TILEENTITY = TILE_ENTITIES.register("crucible_tileentity", () -> TileEntityType.Builder.of(
            () -> new PtCrucibleTileEntity(2000, 100),
            PtBlocks.GRAPHITE_CRUCIBLE.get(),
            PtBlocks.STEEL_CRUCIBLE.get(),
            PtBlocks.QUARTZ_CRUCIBLE.get(),
            PtBlocks.PLATINUM_CRUCIBLE.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PtBurningItemHeaterTile>> HEATER_TILEENTITY = TILE_ENTITIES.register("heater_tileentity", () -> TileEntityType.Builder.of(
            PtBurningItemHeaterTile::new,
            PtBlocks.BURNING_ITEM_HEATER.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PtMirrorTile>> MIRROR_TILEENTITY = TILE_ENTITIES.register("mirror_tileentity", () -> TileEntityType.Builder.of(
            PtMirrorTile::new,
            PtBlocks.MIRROR.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PhotonHeaterTile>> PHOTON_HEATER_TILEENTITY = TILE_ENTITIES.register("photon_heater_tileentity", () -> TileEntityType.Builder.of(
            PhotonHeaterTile::new,
            PtBlocks.VISIBLE_LIGHT_HEATER.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<AxleTile>> AXLE_TILE = TILE_ENTITIES.register("axle_tileentity", () -> TileEntityType.Builder.of(
            () -> new AxleTile(PtTileEntities.AXLE_TILE.get(), 100),
            PtBlocks.AXLE.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PtGearsTile>> GEARS_TILEENTITY = TILE_ENTITIES.register("gears_tileentity", () -> TileEntityType.Builder.of(
            PtGearsTile::new,
            PtBlocks.GEARS_BLOCK.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<DCBrushTilePartA>> DC_BRUSH_TILE_PART_A = TILE_ENTITIES.register("dc_brush_tileentity_part_a", () -> TileEntityType.Builder.of(
            () -> new DCBrushTilePartA(1000),
            PtBlocks.BRUSH_DC_MOTOR_PART_A.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<DCBrushTilePartB>> DC_BRUSH_TILE_PART_B = TILE_ENTITIES.register("dc_brush_tileentity_part_b", () -> TileEntityType.Builder.of(
            () -> new DCBrushTilePartB(1000),
            PtBlocks.BRUSH_DC_MOTOR_PART_B.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PtWireTile>> WIRE = TILE_ENTITIES.register("wire_tileentity", () -> TileEntityType.Builder.of(
            () -> new PtWireTile(1, 10),
            PtBlocks.COPPER_WIRE_1X.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PtInfiniteBatteryTile>> INFINITE_BATTERY = TILE_ENTITIES.register("infinite_battery_tileentity", () -> TileEntityType.Builder.of(
            () -> new PtInfiniteBatteryTile(10.0),
            PtBlocks.INFINITE_BATTERY.get()
    ).build(null));
}
