package photontech.init;

import photontech.PhotonTech;
import photontech.group.PtItemGroups;
import photontech.item.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import photontech.item.ktblockitem.FullAxleBlockItem;

@SuppressWarnings("all")
public class PtItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PhotonTech.ID);

    // MATERIAL
    public static final RegistryObject<Item> SILICON_WAFER_BIG = ITEMS.register("silicon_wafer_small", PtNormalItem::new);
    public static final RegistryObject<Item> SILICON_WAFER_SMALL = ITEMS.register("silicon_wafer_big", PtNormalItem::new);

    public static final RegistryObject<Item> DIAMOND_INGOT = ITEMS.register("diamond_ingot", PtNormalItem::new);
    public static final RegistryObject<Item> QUARTZ_INGOT = ITEMS.register("quartz_ingot", PtNormalItem::new);
    public static final RegistryObject<Item> CORUNDUM_INGOT = ITEMS.register("corundum_ingot", PtNormalItem::new);
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", PtNormalItem::new);
    public static final RegistryObject<Item> SILICON_CRYSTAL = ITEMS.register("silicon_crystal", PtNormalItem::new);
    public static final RegistryObject<Item> SILICON_LOW = ITEMS.register("silicon_low", PtNormalItem::new);
    public static final RegistryObject<Item> SILICON_999 = ITEMS.register("silicon_999", PtNormalItem::new);
    public static final RegistryObject<Item> SILICON_99999 = ITEMS.register("silicon_99999", PtNormalItem::new);
    public static final RegistryObject<Item> SALT = ITEMS.register("salt", PtNormalItem::new);
    public static final RegistryObject<Item> COAL_POWDER = ITEMS.register("coal_powder", PtNormalItem::new);
    public static final RegistryObject<Item> CARBON_BLACK = ITEMS.register("carbon_black", PtNormalItem::new);
    public static final RegistryObject<Item> CARBON_BLACK_TRACE = ITEMS.register("carbon_black_trace", PtNormalItem::new);
    public static final RegistryObject<Item> NETHERITE_BRACKET = ITEMS.register("netherite_bracket", PtNormalItem::new);
    public static final RegistryObject<Item> NETHERITE_BRACKET_HOT = ITEMS.register("netherite_bracket_hot", PtNormalItem::new);
    public static final RegistryObject<Item> TECHNOLOGY_WRENCH = ITEMS.register("technology_wrench", PtNormalItem::new);

    public static final RegistryObject<Item> MONOCRYSTALLINE_SILICON = ITEMS.register("monocrystalline_silicon", PtNormalItem::new);
    public static final RegistryObject<Item> MONOCRYSTALLINE_SILICON_BOLD = ITEMS.register("monocrystalline_silicon_bold", PtNormalItem::new);
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", PtNormalItem::new);
    public static final RegistryObject<Item> RUBY_LENS = ITEMS.register("ruby_lens", PtNormalItem::new);
    public static final RegistryObject<Item> RUBY_ROD = ITEMS.register("ruby_rod", PtNormalItem::new);
    public static final RegistryObject<Item> RUBY_LENS_UNPOLISHED = ITEMS.register("ruby_lens_unpolished", PtNormalItem::new);

    public static final RegistryObject<Item> SAPPHIRE_ROD = ITEMS.register("sapphire_rod", PtNormalItem::new);
    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire", PtNormalItem::new);
    public static final RegistryObject<Item> SAPPHIRE_LENS = ITEMS.register("sapphire_lens", PtNormalItem::new);
    public static final RegistryObject<Item> SAPPHIRE_LENS_UNPOLISHED = ITEMS.register("sapphire_lens_unpolished", PtNormalItem::new);
    public static final RegistryObject<Item> BTC = ITEMS.register("btc", PtNormalItem::new);
//    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", PtNormalItem::new);
//    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", PtNormalItem::new);

    // BLOCK ITEMS
    public static final RegistryObject<Item> RUBY_BLOCK = ITEMS.register("ruby_block", () -> new BlockItem(PtBlocks.RUBY_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> SAPPHIRE_BLOCK = ITEMS.register("sapphire_block", () -> new BlockItem(PtBlocks.SAPPHIRE_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> GRAPHITE_BLOCK = ITEMS.register("graphite_block", () -> new BlockItem(PtBlocks.GRAPHITE_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> STEEL_BLOCK = ITEMS.register("steel_block", () -> new BlockItem(PtBlocks.STEEL_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> COAL_POWDER_BLOCK = ITEMS.register("coal_powder_block", () -> new BlockItem(PtBlocks.COAL_POWDER_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> PLATINUM_BLOCK = ITEMS.register("platinum_block", () -> new BlockItem(PtBlocks.PLATINUM_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
//    public static final RegistryObject<Item> SUN_GLASS = ITEMS.register("sun_glass", () -> new BlockItem(PtBlocks.SUN_GLASS.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
//    public static final RegistryObject<Item> SUN_GLASS_REVERSE = ITEMS.register("sun_glass_reverse", () -> new BlockItem(PtBlocks.SUN_GLASS_REVERSE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));


    // CRUCIBLE BLOCK ITEMS
    public static final RegistryObject<Item> GRAPHITE_CRUCIBLE = ITEMS.register("graphite_crucible", () -> new BlockItem(PtBlocks.GRAPHITE_CRUCIBLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> STEEL_CRUCIBLE = ITEMS.register("steel_crucible", () -> new BlockItem(PtBlocks.STEEL_CRUCIBLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> QUARTZ_CRUCIBLE = ITEMS.register("quartz_crucible", () -> new BlockItem(PtBlocks.QUARTZ_CRUCIBLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> PLATINUM_CRUCIBLE = ITEMS.register("platinum_crucible", () -> new BlockItem(PtBlocks.PLATINUM_CRUCIBLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

    // GAS

    // PHOTON ITEMS
    public static final RegistryObject<Item> MIRROR_FRAME = ITEMS.register("mirror_frame", () -> new BlockItem(PtBlocks.MIRROR_FRAME.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> PROTRACTOR = ITEMS.register("protractor", PtProtractorItem::new);
    public static final RegistryObject<Item> SILVER_MIRROR = ITEMS.register("silver_mirror", MirrorItem::new);


    // FLUID BUCKET ITEMS
//    public static final RegistryObject<Item> NORMAL_FLUID_BUCKET = ITEMS.register("normal_fluid_bucket", () -> new BucketItem(PtFluids.MILK_FLUID, new Item.Properties().tab(ItemGroup.TAB_MATERIALS).craftRemainder(BUCKET)));

    // Tools
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", PtNormalItem::new);

    // CATALYST
    public static final RegistryObject<Item> EMPTY_CATALYST = ITEMS.register("empty_catalyst", () -> new PtCatalyzedItem(10));


    // HEATER BLOCK ITEMS
    public static final RegistryObject<Item> SOLID_HEATER = ITEMS.register("burning_item_heater", () -> new BlockItem(PtBlocks.BURNING_ITEM_HEATER.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> VISIBLE_LIGHT_HEATER = ITEMS.register("visible_light_heater", () -> new BlockItem(PtBlocks.VISIBLE_LIGHT_HEATER.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

    // KINETIC BLOCK ITEMS
    public static final RegistryObject<Item> IRON_AXLE = ITEMS.register("iron_axle", () -> new FullAxleBlockItem(PtBlocks.IRON_AXLE.get()));
    public static final RegistryObject<Item> WOOD_AXLE = ITEMS.register("wood_axle", () -> new FullAxleBlockItem(PtBlocks.WOOD_AXLE.get()));
    public static final RegistryObject<Item> HALF_IRON_AXLE = ITEMS.register("half_iron_axle", () -> new BlockItem(PtBlocks.HALF_IRON_AXLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> HALF_WOOD_AXLE = ITEMS.register("half_wood_axle", () -> new BlockItem(PtBlocks.HALF_WOOD_AXLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> SMALL_GEAR = ITEMS.register("small_gear", () -> new BlockItem(PtBlocks.SMALL_GEAR_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> LARGE_GEAR = ITEMS.register("large_gear", () -> new BlockItem(PtBlocks.LARGE_GEAR_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

    public static final RegistryObject<Item> BRAKE_DISC = ITEMS.register("brake_disc", () -> new BlockItem(PtBlocks.BRAKE_DISC.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> BRAKE_PAD_CONTROLLER = ITEMS.register("brake_pad_controller", () -> new BlockItem(PtBlocks.BRAKE_PAD_CONTROLLER.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));


    // BRUSH DC MOTOR
    public static final RegistryObject<Item> DC_BRUSH_MOTOR_COMMUTATOR = ITEMS.register("dc_brush_motor_commutator", () -> new BlockItem(PtBlocks.DC_BRUSH_MOTOR_COMMUTATOR.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> DC_BRUSH_MOTOR_COIL = ITEMS.register("dc_brush_motor_coil", () -> new BlockItem(PtBlocks.DC_BRUSH_MOTOR_COIL.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> DC_MOTOR_BRUSH = ITEMS.register("dc_motor_brush", PtNormalItem::new);

    public static final RegistryObject<Item> INFINITY_MOTOR = ITEMS.register("infinity_motor", () -> new BlockItem(PtBlocks.INFINITY_MOTOR_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));


    // ELECTRODE ITEMS
    public static final RegistryObject<Item> GRAPHITE_ELECTRODE_1X = ITEMS.register("graphite_electrode_1x", () -> new PtElectrodeItem(PtBlocks.GRAPHITE_ELECTRODE_1X.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> GRAPHITE_ELECTRODE_4X = ITEMS.register("graphite_electrode_4x", () -> new PtElectrodeItem(PtBlocks.GRAPHITE_ELECTRODE_4X.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> GRAPHITE_ELECTRODE_9X = ITEMS.register("graphite_electrode_9x", () -> new PtElectrodeItem(PtBlocks.GRAPHITE_ELECTRODE_9X.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

    // PIPE ITEMS
    public static final RegistryObject<Item> COPPER_WIRE_1X = ITEMS.register("copper_wire_1x", () -> new BlockItem(PtBlocks.COPPER_WIRE_1X.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

    // HYDRAULIC ITEMS
    public static final RegistryObject<Item> HYDRAULIC_PIPE = ITEMS.register("hydraulic_pipe", () -> new BlockItem(PtBlocks.HYDRAULIC_PIPE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> HYDRAULIC_LEVER = ITEMS.register("hydraulic_lever", () -> new BlockItem(PtBlocks.HYDRAULIC_LEVER.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));


    // BATTERY
    public static final RegistryObject<Item> INFINITE_BATTERY = ITEMS.register("infinite_battery", () -> new BlockItem(PtBlocks.INFINITE_BATTERY.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

    // MAGNET
    public static final RegistryObject<Item> FERRITE_MAGNET_PAINTED = ITEMS.register("ferrite_magnet_painted", () -> new BlockItem(PtBlocks.FERRITE_MAGNET_PAINTED.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

}
