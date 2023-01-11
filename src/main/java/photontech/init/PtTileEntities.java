package photontech.init;

import photontech.PhotonTech;
import photontech.block.electric.infiniteBattery.PtInfiniteBatteryTile;
import photontech.block.electric.wire.PtWireTile;
import photontech.block.kinetic.FullAxleTile;
import photontech.block.kinetic.HalfAxleTile;
import photontech.block.heater.photon.PhotonHeaterTile;
import photontech.block.heater.solid.PtBurningItemHeaterTile;
import photontech.block.crucible.PtCrucibleTileEntity;
import photontech.block.kinetic.gears.KtLargeGearTile;
import photontech.block.kinetic.gears.KtSmallGearTile;
import photontech.block.kinetic.motor.dc_brush.DCBrushTilePartA;
import photontech.block.kinetic.motor.dc_brush.DCBrushTilePartB;
import photontech.block.kinetic.motor.infinity.InfinityMotorTile;
import photontech.block.magnet.permanent.PermanentMagnetTile;
import photontech.block.mirror.PtMirrorTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("all")
public class PtTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, PhotonTech.ID);

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

    public static final RegistryObject<TileEntityType<FullAxleTile>> AXLE_TILE = TILE_ENTITIES.register("axle_tileentity", () -> TileEntityType.Builder.of(
            () -> new FullAxleTile(PtTileEntities.AXLE_TILE.get(), 1),
            PtBlocks.IRON_AXLE.get(),
            PtBlocks.WOOD_AXLE.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<HalfAxleTile>> HALF_AXLE_TILE = TILE_ENTITIES.register("half_axle_tileentity", () -> TileEntityType.Builder.of(
            () -> new HalfAxleTile(PtTileEntities.HALF_AXLE_TILE.get(), 1),
            PtBlocks.HALF_IRON_AXLE.get(),
            PtBlocks.HALF_WOOD_AXLE.get()
    ).build(null));


    public static final RegistryObject<TileEntityType<KtSmallGearTile>> SMALL_GEARS_TILEENTITY = TILE_ENTITIES.register("small_gears_tileentity", () -> TileEntityType.Builder.of(
            () -> new KtSmallGearTile(1),
            PtBlocks.SMALL_GEAR_BLOCK.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<KtLargeGearTile>> LARGE_GEARS_TILEENTITY = TILE_ENTITIES.register("large_gears_tileentity", () -> TileEntityType.Builder.of(
            () -> new KtLargeGearTile(1),
            PtBlocks.LARGE_GEAR_BLOCK.get()
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

    public static final RegistryObject<TileEntityType<PermanentMagnetTile>> PERMANENT_MAGNET = TILE_ENTITIES.register("permanent_magnet_tileentity", () -> TileEntityType.Builder.of(
            () -> new PermanentMagnetTile(1.0),
            PtBlocks.FERRITE_MAGNET_PAINTED.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<InfinityMotorTile>> INFINITY_MOTOR = TILE_ENTITIES.register("infinity_motor_tileentity", () -> TileEntityType.Builder.of(
            () -> new InfinityMotorTile(1),
            PtBlocks.INFINITY_MOTOR_BLOCK.get()
    ).build(null));

}
