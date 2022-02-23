package photontech.init.tag;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import photontech.utils.Utils;

public class PtItemTags {
    public static final ITag.INamedTag<Item> CATALYST_PT = tag("catalyst_pt");

    private static ITag.INamedTag<Item> tag(String name) {
        return ItemTags.bind(new ResourceLocation(Utils.MOD_ID, name).toString());
    }
}
