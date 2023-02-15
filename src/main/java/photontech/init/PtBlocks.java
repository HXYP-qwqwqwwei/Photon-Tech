package photontech.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import photontech.PhotonTech;
import photontech.block.electric.electrode.PtElectrodeBlock;
import photontech.block.electric.infiniteBattery.InfiniteBatteryBlock;
import photontech.block.electric.wire.WireBlock;
import photontech.block.hydraulic.HydraulicLeverBlock;
import photontech.block.hydraulic.HydraulicPipeBlock;
import photontech.block.kinetic.AxleMaterial;
import photontech.block.kinetic.HalfAxle.HalfAxleBlock;
import photontech.block.kinetic.axle.FullAxleBlock;
import photontech.block.kinetic.KineticRotatingBlock;
import photontech.block.heater.photon.PhotonHeaterBlock;
import photontech.block.heater.solid.PtBurningItemHeaterBlock;
import photontech.block.PtPowderBlock;
import photontech.block.crucible.PtCrucibleBlock;
import photontech.block.PtNormalBlock;
import photontech.block.kinetic.brake.BrakeDiscBlock;
import photontech.block.kinetic.brake.BrakePadControllerBlock;
import photontech.block.kinetic.gears.GearBlock;
import photontech.block.kinetic.motor.dcbrush.DCBrushMotorCommutatorBlock;
import photontech.block.kinetic.motor.infinity.InfinityMotorBlock;
import photontech.block.magnet.permanent.PermanentMagnetBlock;
import photontech.block.light.mirror.MirrorFrameBlock;
import photontech.block.kinetic.motor.dcbrush.DCBrushMotorCoilBlock;
import photontech.utils.block.PipeLikeBlock;

public class PtBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PhotonTech.ID);
    public static final RegistryObject<Block> RUBY_BLOCK = BLOCKS.register("ruby_block", PtNormalBlock::new);
    public static final RegistryObject<Block> SAPPHIRE_BLOCK = BLOCKS.register("sapphire_block", PtNormalBlock::new);
    public static final RegistryObject<Block> GRAPHITE_BLOCK = BLOCKS.register("graphite_block", PtNormalBlock::new);
    public static final RegistryObject<Block> STEEL_BLOCK = BLOCKS.register("steel_block", PtNormalBlock::new);
    public static final RegistryObject<Block> PLATINUM_BLOCK = BLOCKS.register("platinum_block", PtNormalBlock::new);

    public static final RegistryObject<Block> COAL_POWDER_BLOCK = BLOCKS.register("coal_powder_block", PtPowderBlock::new);

    // CRUCIBLE BLOCKS
    public static final RegistryObject<Block> GRAPHITE_CRUCIBLE = BLOCKS.register("graphite_crucible", () -> new PtCrucibleBlock(4000, 126));
    public static final RegistryObject<Block> STEEL_CRUCIBLE = BLOCKS.register("steel_crucible", () -> new PtCrucibleBlock(1800, 40));
    public static final RegistryObject<Block> QUARTZ_CRUCIBLE = BLOCKS.register("quartz_crucible", () -> new PtCrucibleBlock(2000, 7));
    public static final RegistryObject<Block> PLATINUM_CRUCIBLE = BLOCKS.register("platinum_crucible", () -> new PtCrucibleBlock(2000, 70));

    // HEATER BLOCKS
    public static final RegistryObject<Block> BURNING_ITEM_HEATER = BLOCKS.register("burning_item_heater", PtBurningItemHeaterBlock::new);
    public static final RegistryObject<Block> VISIBLE_LIGHT_HEATER = BLOCKS.register("visible_light_heater", PhotonHeaterBlock::new);
//    public static final RegistryObject<Block> LIQUID_HEATER = BLOCKS.register("liquid_heater", PtHeaterBlock::new);

    // Pt BLOCKS
    public static final RegistryObject<Block> MIRROR_FRAME = BLOCKS.register("mirror_frame", MirrorFrameBlock::new);

    // KINETIC BLOCKS
    public static final RegistryObject<FullAxleBlock> IRON_AXLE = BLOCKS.register("iron_axle", () -> new FullAxleBlock(AxleMaterial.IRON));
    public static final RegistryObject<FullAxleBlock> WOOD_AXLE = BLOCKS.register("wood_axle", () -> new FullAxleBlock(AxleMaterial.WOOD));

    public static final RegistryObject<HalfAxleBlock> HALF_IRON_AXLE = BLOCKS.register("half_iron_axle", () -> new HalfAxleBlock(AxleMaterial.IRON));
    public static final RegistryObject<HalfAxleBlock> HALF_WOOD_AXLE = BLOCKS.register("half_wood_axle", () -> new HalfAxleBlock(AxleMaterial.WOOD));

    public static final RegistryObject<Block> SMALL_GEAR_BLOCK = BLOCKS.register("small_gear_block", () -> new GearBlock(8, GearBlock.GearType.SMALL_GEAR));
    public static final RegistryObject<Block> LARGE_GEAR_BLOCK = BLOCKS.register("large_gear_block", () -> new GearBlock(16, GearBlock.GearType.LARGE_GEAR));
    public static final RegistryObject<KineticRotatingBlock> DC_BRUSH_MOTOR_COMMUTATOR = BLOCKS.register("dc_brush_motor_commutator", DCBrushMotorCommutatorBlock::new);
    public static final RegistryObject<KineticRotatingBlock> DC_BRUSH_MOTOR_COIL = BLOCKS.register("dc_brush_motor_coil", DCBrushMotorCoilBlock::new);
    public static final RegistryObject<InfinityMotorBlock> INFINITY_MOTOR_BLOCK = BLOCKS.register("infinity_motor", InfinityMotorBlock::new);

    public static final RegistryObject<BrakeDiscBlock> BRAKE_DISC = BLOCKS.register("brake_disc", () -> new BrakeDiscBlock(16));
    public static final RegistryObject<BrakePadControllerBlock> BRAKE_PAD_CONTROLLER = BLOCKS.register("brake_pad_controller", BrakePadControllerBlock::new);


    // WIRE BLOCKS
    public static final RegistryObject<Block> GRAPHITE_ELECTRODE_1X = BLOCKS.register("graphite_electrode_1x", () -> new PtElectrodeBlock(PipeLikeBlock.Thickness.SIZE_1X));
    public static final RegistryObject<Block> GRAPHITE_ELECTRODE_4X = BLOCKS.register("graphite_electrode_4x", () -> new PtElectrodeBlock(PipeLikeBlock.Thickness.SIZE_4X));
    public static final RegistryObject<Block> GRAPHITE_ELECTRODE_9X = BLOCKS.register("graphite_electrode_9x", () -> new PtElectrodeBlock(PipeLikeBlock.Thickness.SIZE_9X));

    // HYDRAULIC_BLOCKS
    public static final RegistryObject<Block> HYDRAULIC_PIPE = BLOCKS.register("hydraulic_pipe", HydraulicPipeBlock::new);
    public static final RegistryObject<Block> HYDRAULIC_LEVER = BLOCKS.register("hydraulic_lever", HydraulicLeverBlock::new);


    // ELECTRODE COVER
//    public static final RegistryObject<Block> GLASS_COVER = BLOCKS.register("glass_cover", () -> new PermanentMagnetBlock(7, 10.0));

    // WIRE BLOCKS
    public static final RegistryObject<Block> COPPER_WIRE_1X = BLOCKS.register("copper_wire_1x", () -> new WireBlock(PipeLikeBlock.Thickness.SIZE_1X, 20));

    public static final RegistryObject<Block> INFINITE_BATTERY = BLOCKS.register("infinite_battery", InfiniteBatteryBlock::new);

    // MAGNET BLOCKS
    public static final RegistryObject<Block> FERRITE_MAGNET_PAINTED = BLOCKS.register("ferrite_magnet_painted", () -> new PermanentMagnetBlock(7, 0.1));


}
