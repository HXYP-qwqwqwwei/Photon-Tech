package photontech.init;

import photontech.group.PtItemGroups;
import photontech.item.PtNormalItem;
import photontech.item.PtProtractorItem;
import photontech.utils.Utils;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PtItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Utils.MOD_ID);

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
//    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", PtNormalItem::new);
//    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", PtNormalItem::new);


    // NORMAL BLOCK ITEMS
    public static final RegistryObject<Item> RUBY_BLOCK = ITEMS.register("ruby_block", () -> new BlockItem(PtBlocks.RUBY_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> SAPPHIRE_BLOCK = ITEMS.register("sapphire_block", () -> new BlockItem(PtBlocks.SAPPHIRE_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> GRAPHITE_BLOCK = ITEMS.register("graphite_block", () -> new BlockItem(PtBlocks.GRAPHITE_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> STEEL_BLOCK = ITEMS.register("steel_block", () -> new BlockItem(PtBlocks.STEEL_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> COAL_POWDER_BLOCK = ITEMS.register("coal_powder_block", () -> new BlockItem(PtBlocks.COAL_POWDER_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> PLATINUM_BLOCK = ITEMS.register("platinum_block", () -> new BlockItem(PtBlocks.PLATINUM_BLOCK.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> SUN_GLASS = ITEMS.register("sun_glass", () -> new BlockItem(PtBlocks.SUN_GLASS.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> SUN_GLASS_REVERSE = ITEMS.register("sun_glass_reverse", () -> new BlockItem(PtBlocks.SUN_GLASS_REVERSE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

    // CRUCIBLE BLOCK ITEMS
    public static final RegistryObject<Item> GRAPHITE_CRUCIBLE = ITEMS.register("graphite_crucible", () -> new BlockItem(PtBlocks.GRAPHITE_CRUCIBLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> STEEL_CRUCIBLE = ITEMS.register("steel_crucible", () -> new BlockItem(PtBlocks.STEEL_CRUCIBLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> QUARTZ_CRUCIBLE = ITEMS.register("quartz_crucible", () -> new BlockItem(PtBlocks.QUARTZ_CRUCIBLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> PLATINUM_CRUCIBLE = ITEMS.register("platinum_crucible", () -> new BlockItem(PtBlocks.PLATINUM_CRUCIBLE.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));

    // PHOTON ITEMS
    public static final RegistryObject<Item> MIRROR = ITEMS.register("mirror", () -> new BlockItem(PtBlocks.MIRROR.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> PROTRACTOR = ITEMS.register("protractor", PtProtractorItem::new);


    // FLUID BUCKET ITEMS
//    public static final RegistryObject<Item> NORMAL_FLUID_BUCKET = ITEMS.register("normal_fluid_bucket", () -> new BucketItem(PtFluids.MILK_FLUID, new Item.Properties().tab(ItemGroup.TAB_MATERIALS).craftRemainder(BUCKET)));

    // Tools
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", PtNormalItem::new);

    // HEATER BLOCK ITEMS
    public static final RegistryObject<Item> SOLID_HEATER = ITEMS.register("burning_item_heater", () -> new BlockItem(PtBlocks.BURNING_ITEM_HEATER.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));
    public static final RegistryObject<Item> VISIBLE_LIGHT_HEATER = ITEMS.register("visible_light_heater", () -> new BlockItem(PtBlocks.VISIBLE_LIGHT_HEATER.get(), new Item.Properties().tab(PtItemGroups.BLOCK_GROUP)));


}
