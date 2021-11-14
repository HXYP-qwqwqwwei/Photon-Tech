package photontech.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import photontech.block.crucible.PtCrucibleBlock;

import javax.annotation.Nonnull;

public class PtElectrodeItem extends BlockItem {

    public PtElectrodeItem(Block p_i48527_1_, Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Nonnull
    @Override
    public ActionResultType place(BlockItemUseContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos().relative(Direction.DOWN)).getBlock() instanceof PtCrucibleBlock) {
            return super.place(context);
        }
        return ActionResultType.FAIL;
    }
}
