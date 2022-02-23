package photontech.init;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import photontech.block.electric.electrode.PtElectrodeBlock;
import photontech.block.electric.infiniteBattery.PtInfiniteBatteryBlock;
import photontech.block.electric.wire.PtWireBlock;
import photontech.block.kinetic.gears.PtGearsBlock;
import photontech.block.PtModifiedGlassBlock;
import photontech.block.kinetic.axle.AxleBlock;
import photontech.block.heater.photon.PhotonHeaterBlock;
import photontech.block.heater.solid.PtBurningItemHeaterBlock;
import photontech.block.PtPowderBlock;
import photontech.block.crucible.PtCrucibleBlock;
import photontech.block.PtNormalBlock;
import photontech.block.kinetic.motor.dc_brush.DCMotorBlockPartA;
import photontech.block.magnet.MagnetBlock;
import photontech.block.magnet.permanent.PermanentMagnetBlock;
import photontech.block.mirror.PtMirrorBlock;
import photontech.block.kinetic.motor.dc_brush.DCMotorBlockPartB;
import photontech.utils.Utils;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import photontech.utils.block.PipeLikeBlock;

public class PtBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Utils.MOD_ID);
    public static final RegistryObject<Block> RUBY_BLOCK = BLOCKS.register("ruby_block", PtNormalBlock::new);
    public static final RegistryObject<Block> SAPPHIRE_BLOCK = BLOCKS.register("sapphire_block", PtNormalBlock::new);
    public static final RegistryObject<Block> GRAPHITE_BLOCK = BLOCKS.register("graphite_block", PtNormalBlock::new);
    public static final RegistryObject<Block> STEEL_BLOCK = BLOCKS.register("steel_block", PtNormalBlock::new);
    public static final RegistryObject<Block> PLATINUM_BLOCK = BLOCKS.register("platinum_block", PtNormalBlock::new);

    public static final RegistryObject<Block> COAL_POWDER_BLOCK = BLOCKS.register("coal_powder_block", PtPowderBlock::new);

    public static final RegistryObject<Block> SUN_GLASS = BLOCKS.register("sun_glass", PtModifiedGlassBlock::new);
    public static final RegistryObject<Block> SUN_GLASS_REVERSE = BLOCKS.register("sun_glass_reverse", () -> new PtModifiedGlassBlock() {
                @Override
                public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
                    return false;
                }

                @Override
                public int getLightBlock(BlockState state, IBlockReader world, BlockPos pos) {
                    return world.getMaxLightLevel();
                }

                @Override
                public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
                    return 0.2F;
                }
            }
    );


    // CRUCIBLE BLOCKS
    public static final RegistryObject<Block> GRAPHITE_CRUCIBLE = BLOCKS.register("graphite_crucible", () -> new PtCrucibleBlock(4000, 126));
    public static final RegistryObject<Block> STEEL_CRUCIBLE = BLOCKS.register("steel_crucible", () -> new PtCrucibleBlock(1800, 40));
    public static final RegistryObject<Block> QUARTZ_CRUCIBLE = BLOCKS.register("quartz_crucible", () -> new PtCrucibleBlock(2000, 7));
    public static final RegistryObject<Block> PLATINUM_CRUCIBLE = BLOCKS.register("platinum_crucible", () -> new PtCrucibleBlock(2000, 70));

    // FLUID BLOCKS
//    public static final RegistryObject<FlowingFluidBlock> MILK_FLUID = BLOCKS.register("milk_fluid", () -> new FlowingFluidBlock(PtFluids.MILK_FLUID, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));

    // HEATER BLOCKS
    public static final RegistryObject<Block> BURNING_ITEM_HEATER = BLOCKS.register("burning_item_heater", PtBurningItemHeaterBlock::new);
    public static final RegistryObject<Block> VISIBLE_LIGHT_HEATER = BLOCKS.register("visible_light_heater", PhotonHeaterBlock::new);
//    public static final RegistryObject<Block> LIQUID_HEATER = BLOCKS.register("liquid_heater", PtHeaterBlock::new);

    // Pt BLOCKS
    public static final RegistryObject<Block> MIRROR = BLOCKS.register("mirror", PtMirrorBlock::new);

    // KINETIC BLOCKS
    public static final RegistryObject<Block> AXLE = BLOCKS.register("axle", () -> new AxleBlock(16, 4));
    public static final RegistryObject<Block> GEARS_BLOCK = BLOCKS.register("gears_block", PtGearsBlock::new);
    public static final RegistryObject<Block> BRUSH_DC_MOTOR_PART_A = BLOCKS.register("brush_dc_motor_part_a", DCMotorBlockPartA::new);
    public static final RegistryObject<Block> BRUSH_DC_MOTOR_PART_B = BLOCKS.register("brush_dc_motor_part_b", DCMotorBlockPartB::new);

    // ELECTRODE BLOCKS
    public static final RegistryObject<Block> GRAPHITE_ELECTRODE_1X = BLOCKS.register("graphite_electrode_1x", () -> new PtElectrodeBlock(PipeLikeBlock.Thickness.SIZE_1X));
    public static final RegistryObject<Block> GRAPHITE_ELECTRODE_4X = BLOCKS.register("graphite_electrode_4x", () -> new PtElectrodeBlock(PipeLikeBlock.Thickness.SIZE_4X));
    public static final RegistryObject<Block> GRAPHITE_ELECTRODE_9X = BLOCKS.register("graphite_electrode_9x", () -> new PtElectrodeBlock(PipeLikeBlock.Thickness.SIZE_9X));

    // WIRE BLOCKS
    public static final RegistryObject<Block> COPPER_WIRE_1X = BLOCKS.register("copper_wire_1x", () -> new PtWireBlock(PipeLikeBlock.Thickness.SIZE_1X, 1.75e-8));

    public static final RegistryObject<Block> INFINITE_BATTERY = BLOCKS.register("infinite_battery", PtInfiniteBatteryBlock::new);

    // MAGNET BLOCKS
    public static final RegistryObject<Block> FERRITE_MAGNET_PAINTED = BLOCKS.register("ferrite_magnet_painted", () -> new PermanentMagnetBlock(7, 10.0));

}
