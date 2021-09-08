package photontech.item;

import photontech.group.PtItemGroups;
import net.minecraft.item.Item;

import net.minecraft.item.Item.Properties;

public class PtNormalItem extends Item {
    public PtNormalItem() {
        super(new Properties().tab(PtItemGroups.NORMAL_ITEM_GROUP));
    }
}
