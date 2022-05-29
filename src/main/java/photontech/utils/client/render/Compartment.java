package photontech.utils.client.render;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import com.jozufozu.flywheel.core.PartialModel;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

@SuppressWarnings("all")
public class Compartment<T> {
    public static final Compartment<BlockState> GENERIC_TILE = new Compartment<>();
    public static final Compartment<PartialModel> PARTIAL = new Compartment<>();
    public static final Compartment<Pair<Direction, PartialModel>> DIRECTIONAL_PARTIAL = new Compartment<>();

    public static final Compartment<BlockState> BLOCK_MODEL = new Compartment<>();
    public static final Compartment<ItemStack> ITEM_MODEL = new Compartment<>();
    public static final Compartment<ResourceLocation> GENERIC_MODEL = new Compartment<>();
}
