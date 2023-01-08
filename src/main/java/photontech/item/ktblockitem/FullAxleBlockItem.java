package photontech.item.ktblockitem;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import photontech.block.kinetic.IAxleBlock;
import photontech.block.kinetic.KtMachineTile;
import photontech.group.PtItemGroups;

import javax.annotation.Nonnull;

public class FullAxleBlockItem extends BlockItem {

    public <T extends Block & IAxleBlock> FullAxleBlockItem(T block) {
        super(block, new Item.Properties().tab(PtItemGroups.BLOCK_GROUP));
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World level = context.getLevel();
        ItemStack itemInHand = context.getItemInHand();

        PlayerEntity player = context.getPlayer();
        if (player != null && !player.isShiftKeyDown()) {
            if (itemInHand.getItem() instanceof FullAxleBlockItem) {

                TileEntity tile = level.getBlockEntity(pos);
                if (tile instanceof KtMachineTile) {

                    KtMachineTile kt = (KtMachineTile) tile;
                    if (kt.canAddAxle()) {
                        kt.insertAxle((FullAxleBlockItem) itemInHand.getItem());
                        MinecraftForge.EVENT_BUS.post(kt.createKtCreateEvent());
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }
        return super.useOn(context);
    }
}
