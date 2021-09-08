package photontech.init;

import photontech.block.heater.photon.PhotonHeaterTile;
import photontech.block.heater.solid.PtHeaterTileEntity;
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

    public static final RegistryObject<TileEntityType<PtHeaterTileEntity>> HEATER_TILEENTITY = TILE_ENTITIES.register("heater_tileentity", () -> TileEntityType.Builder.of(
            PtHeaterTileEntity::new,
            PtBlocks.SOLID_HEATER.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PtMirrorTile>> MIRROR_TILEENTITY = TILE_ENTITIES.register("mirror_tileentity", () -> TileEntityType.Builder.of(
            PtMirrorTile::new,
            PtBlocks.MIRROR.get()
    ).build(null));

    public static final RegistryObject<TileEntityType<PhotonHeaterTile>> PHOTON_HEATER_TILEENTITY = TILE_ENTITIES.register("photon_heater_tileentity", () -> TileEntityType.Builder.of(
            PhotonHeaterTile::new,
            PtBlocks.PHOTON_HEATER.get()
    ).build(null));

}
