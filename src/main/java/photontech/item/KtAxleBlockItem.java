package photontech.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import photontech.block.kinetic.axle.KtMachineTile;
import photontech.block.kinetic.axle.KtRotatingBlock;
import photontech.event.pt_events.AxleInsertEvent;
import photontech.group.PtItemGroups;

import javax.annotation.Nonnull;

public class KtAxleBlockItem extends BlockItem {

    public KtAxleBlockItem(KtRotatingBlock block) {
        super(block, new Properties().tab(PtItemGroups.NORMAL_ITEM_GROUP));
    }

    @Nonnull
    @Override
    public KtRotatingBlock getBlock() {
        return (KtRotatingBlock) super.getBlock();
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World level = context.getLevel();
        ItemStack itemInHand = context.getItemInHand();

        PlayerEntity player = context.getPlayer();
        if (player != null && !player.isShiftKeyDown()) {
            if (itemInHand.getItem() instanceof KtAxleBlockItem) {

                TileEntity tile = level.getBlockEntity(pos);
                if (tile instanceof KtMachineTile) {

                    KtMachineTile ktMachineTile = (KtMachineTile) tile;
                    if (ktMachineTile.canAddAxle()) {

                        ktMachineTile.insertAxle((KtAxleBlockItem) itemInHand.getItem());
                        AxleInsertEvent event = new AxleInsertEvent(level, pos, level.getBlockState(pos));
                        MinecraftForge.EVENT_BUS.post(event);
                        if (event.isCanceled()) {
                            ktMachineTile.removeAxle();
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }
        return super.useOn(context);
    }
}
