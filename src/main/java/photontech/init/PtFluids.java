package photontech.init;

import net.minecraft.item.Items;
import photontech.PhotonTech;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PtFluids {

    public static final ResourceLocation MELTING_FLUID_STILL_TEXTURE = new ResourceLocation(PhotonTech.ID, "block/melting_fluid_still");
    public static final ResourceLocation MELTING_FLUID_FLOWING_TEXTURE = new ResourceLocation(PhotonTech.ID, "block/melting_fluid_flow");

//    public static final ResourceLocation STILL_NORMAL_FLUID_TEXTURE = new ResourceLocation("block/water_still");
//    public static final ResourceLocation FLOWING_NORMAL_FLUID_TEXTURE = new ResourceLocation("block/water_flow");

    public static final ResourceLocation SOLID_WATER_STILL_TEXTURE = new ResourceLocation(PhotonTech.ID, "block/water_still");
    public static final ResourceLocation SOLID_WATER_FLOWING_TEXTURE = new ResourceLocation(PhotonTech.ID, "block/water_flow");


//    public static final ResourceLocation STILL_MELTING_FLUID_TEXTURE = new ResourceLocation("block/water_still");
//    public static final ResourceLocation FLOWING_MELTING_FLUID_TEXTURE = new ResourceLocation("block/water_still");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, PhotonTech.ID);
    public static RegistryObject<FlowingFluid> MILK_FLUID = FLUIDS.register("milk_fluid", () -> new ForgeFlowingFluid.Source(PtFluids.PROPERTIES));
    public static RegistryObject<FlowingFluid> MILK_FLUID_FLOWING = FLUIDS.register("milk_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(PtFluids.PROPERTIES));
    public static ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(MILK_FLUID, MILK_FLUID_FLOWING, FluidAttributes.builder(SOLID_WATER_STILL_TEXTURE, SOLID_WATER_FLOWING_TEXTURE).color(0xFFFFFFFF).density(1020))
            .bucket(() -> Items.MILK_BUCKET)
//            .block(PtBlocks.NORMAL_FLUID)
            .slopeFindDistance(3)
            .explosionResistance(100F);

}
