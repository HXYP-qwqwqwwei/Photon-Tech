package photontech.init;

import photontech.PhotonTech;
import photontech.block.electric.infiniteBattery.InfiniteBatteryTile;
import photontech.block.electric.wire.WireTile;
import photontech.block.kinetic.FullAxleTile;
import photontech.block.kinetic.HalfAxleTile;
import photontech.block.heater.photon.PhotonHeaterTile;
import photontech.block.heater.solid.PtBurningItemHeaterTile;
import photontech.block.crucible.PtCrucibleTileEntity;
import photontech.block.kinetic.gears.LargeGearTile;
import photontech.block.kinetic.gears.SmallGearTile;
import photontech.block.kinetic.motor.dcbrush.DCBrushMotorCommutatorTile;
import photontech.block.kinetic.motor.dcbrush.DCBrushMotorCoilTile;
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


    public static final RegistryObject<TileEntityType<SmallGearTile>> SMALL_GEARS_TILEENTITY = TILE_ENTITIES.register("small_gears_tileentity", () -> TileEntityType.Builder.of(
            () -> new SmallGearTile(1),
            PtBlocks.SMALL_GEAR_BLOCK.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<LargeGearTile>> LARGE_GEARS_TILEENTITY = TILE_ENTITIES.register("large_gears_tileentity", () -> TileEntityType.Builder.of(
            () -> new LargeGearTile(1),
            PtBlocks.LARGE_GEAR_BLOCK.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<DCBrushMotorCommutatorTile>> DC_BRUSH_TILE_PART_A = TILE_ENTITIES.register("dc_brush_tileentity_part_a", () -> TileEntityType.Builder.of(
            () -> new DCBrushMotorCommutatorTile(1000),
            PtBlocks.DC_BRUSH_MOTOR_COMMUTATOR.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<DCBrushMotorCoilTile>> DC_BRUSH_TILE_PART_B = TILE_ENTITIES.register("dc_brush_tileentity_part_b", () -> TileEntityType.Builder.of(
            () -> new DCBrushMotorCoilTile(1000),
            PtBlocks.DC_BRUSH_MOTOR_COIL.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<WireTile>> WIRE = TILE_ENTITIES.register("wire_tileentity", () -> TileEntityType.Builder.of(
            () -> new WireTile(10),
            PtBlocks.COPPER_WIRE_1X.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<InfiniteBatteryTile>> INFINITE_BATTERY = TILE_ENTITIES.register("infinite_battery_tileentity", () -> TileEntityType.Builder.of(
            () -> new InfiniteBatteryTile(10.0),
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
