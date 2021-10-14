package photontech.init;

import photontech.block.kinetic.axle.AxleTile;
import photontech.block.kinetic.gears.PtGearsTile;
import photontech.block.heater.photon.PhotonHeaterTile;
import photontech.block.heater.solid.PtBurningItemHeaterTile;
import photontech.block.crucible.PtCrucibleTileEntity;
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
            AxleTile::new,
            PtBlocks.AXLE.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PtGearsTile>> GEARS_TILEENTITY = TILE_ENTITIES.register("gears_tileentity", () -> TileEntityType.Builder.of(
            PtGearsTile::new,
            PtBlocks.GEARS_BLOCK.get()
    ).build(null));

}
