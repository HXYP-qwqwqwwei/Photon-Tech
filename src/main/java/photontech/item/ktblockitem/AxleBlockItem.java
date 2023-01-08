//package photontech.item.ktblockitem;
//
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.ItemUseContext;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.common.MinecraftForge;
//import photontech.block.kinetic.KtMachineTile;
//import photontech.block.kinetic.KtRotatingBlock;
//import photontech.event.pt_events.AxleActiveEvent;
//import photontech.group.PtItemGroups;
//
//import javax.annotation.Nonnull;
//
//public abstract class AxleBlockItem extends BlockItem {
//    public AxleBlockItem(KtRotatingBlock block) {
//        super(block, new Item.Properties().tab(PtItemGroups.BLOCK_GROUP));
//    }
//
//    @Nonnull
//    @Override
//    public KtRotatingBlock getBlock() {
//        return (KtRotatingBlock) super.getBlock();
//    }
//
//    /**
//     * 判断目标TE是否可以进行交互
//     * 子类需要覆写这个方法以指定哪些TE可以和此类物品交互
//     * @param target 目标TE
//     * @return 目标TE是否符合条件
//     */
//    public abstract boolean fits(TileEntity target);
//
//    @Nonnull
//    @Override
//    public ActionResultType useOn(ItemUseContext context) {
//        BlockPos pos = context.getClickedPos();
//        World level = context.getLevel();
//        ItemStack itemInHand = context.getItemInHand();
//
//        PlayerEntity player = context.getPlayer();
//        if (player != null && !player.isShiftKeyDown()) {
//            if (itemInHand.getItem() instanceof FullAxleBlockItem) {
//
//                TileEntity tile = level.getBlockEntity(pos);
//                if (tile instanceof KtMachineTile) {
//
//                    KtMachineTile ktMachineTile = (KtMachineTile) tile;
//                    if (ktMachineTile.canAddAxle()) {
//
//                        ktMachineTile.insertAxle((FullAxleBlockItem) itemInHand.getItem());
//                        AxleActiveEvent event = new AxleActiveEvent(level, pos, level.getBlockState(pos));
//                        MinecraftForge.EVENT_BUS.post(event);
//                        if (event.isCanceled()) {
//                            ktMachineTile.removeAxle();
//                        }
//                        return ActionResultType.SUCCESS;
//                    }
//                }
//            }
//        }
//        return super.useOn(context);
//    }
//
//}
