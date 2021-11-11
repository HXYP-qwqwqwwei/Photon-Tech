package photontech.utils;


import net.minecraft.state.BooleanProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import photontech.utils.recipe.PtConditionalRecipe;
import photontech.utils.recipe.RecipeCondition;

import java.util.Comparator;

public class PtConstants {
    public static final String GROUP = "group";
    public static final String CONDITION = "condition";
    public static final String RATE = "rate";

    // Thermal
    public static final String THERMAL_CONDITION = "thermal_condition";
    public static final String TEMPERATURE = "temperature";
    public static final String HEAT = "heat";

    // Electric
    public static final String ELECTRIC_CONDITION = "electric_condition";
    public static final String VOLTAGE = "voltage";
    public static final String ELECTRON = "energy";

    // Photometry
    public static final String PHOTOMETRY_CONDITION = "photometry_condition";
    public static final String FREQUENCY = "wave_length";
    public static final String PHOTON = "photon";

    // Fluid
    public static final String INPUT_FLUIDS = "input_fluids";
    public static final String OUTPUT_FLUIDS = "output_fluids";
    public static final String FLUID = "fluid";
    public static final String AMOUNT = "amount";

    // Item
    public static final String INPUT_ITEMS = "input_items";
    public static final String OUTPUT_ITEMS = "output_items";
    public static final String ITEM = "item";
    public static final String COUNT = "count";

    // Properties
    public static final BooleanProperty HOLDING_INPUT = BooleanProperty.create("holding_input");

    public static class MODELS {
        // MIRROR
        public static final ResourceLocation MIRROR_FRAME = new ResourceLocation(Utils.MOD_ID, "special/frame");
        public static final ResourceLocation MIRROR_SUPPORT = new ResourceLocation(Utils.MOD_ID, "special/support");
        public static final ResourceLocation SILVER_MIRROR = new ResourceLocation(Utils.MOD_ID, "special/silver_mirror");

        // DC BRUSH MOTOR
        public static final ResourceLocation DC_BRUSH_MODEL_PART_A_BRUSH = new ResourceLocation(Utils.MOD_ID, "block/brush_motor_models/part_a_brush_model");
        public static final ResourceLocation DC_BRUSH_MODEL_PART_A_CONTACTOR = new ResourceLocation(Utils.MOD_ID, "block/brush_motor_models/part_a_contactor_model");
        public static final ResourceLocation DC_BRUSH_MODEL_PART_A_WIRES = new ResourceLocation(Utils.MOD_ID, "block/brush_motor_models/wires_model");
        public static final ResourceLocation DC_BRUSH_MODEL_PART_B = new ResourceLocation(Utils.MOD_ID, "block/brush_motor_models/part_b_model");

        public static final ResourceLocation IRON_AXLE_MODEL = new ResourceLocation(Utils.MOD_ID, "special/iron_axle_model");
        public static final ResourceLocation WOODEN_GEAR_MODEL = new ResourceLocation(Utils.MOD_ID, "special/wooden_gear_model");
    }


    public static final Comparator<PtConditionalRecipe> NATURAL_HEAT_RECIPE_COMPARATOR = (r1, r2) -> {
        RecipeCondition c1 = r1.getCondition();
        RecipeCondition c2 = r2.getCondition();
        if (c1.heat <= 0 && c2.heat <= 0) {
            return (int) Math.ceil(c2.temperature - c1.temperature);
        }
        if (c1.heat > 0 && c2.heat > 0) {
            return (int) Math.ceil(c1.temperature - c2.temperature);
        }
        else return (int) Math.ceil(c2.rate - c1.rate);
    };
}
