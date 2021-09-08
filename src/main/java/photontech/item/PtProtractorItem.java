package photontech.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.Item.Properties;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import photontech.block.mirror.PtMirrorTile;
import photontech.init.PtBlocks;
import photontech.init.PtItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PtProtractorItem extends PtNormalItem {

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable World world, @Nonnull List<ITextComponent> list, @Nonnull ITooltipFlag flag) {

        super.appendHoverText(itemStack, world, list, flag);
        CompoundNBT tag = itemStack.getOrCreateTag();
        for (int i = 1; i <= 2; ++i) {
            String dimension = tag.getString("RefDimension" + i);
            if (!dimension.isEmpty()) {
                BlockPos pos = BlockPos.of(tag.getLong("RefPos" + i));
                list.add(new TranslationTextComponent(
                        "text.photontech.pos",
                        i + ":[" + dimension + "]" + pos.toShortString()
                ));
            }
        }
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        ItemStack protractor = context.getItemInHand();
        CompoundNBT tag = protractor.getOrCreateTag();

        if (Objects.requireNonNull(context.getPlayer()).isShiftKeyDown()) {

            if (tag.getString("RefDimension1").isEmpty()) {
                tag.putLong("RefPos1", pos.asLong());
                tag.putString("RefDimension1", world.dimension().location().toString());
            }
            else if (tag.getString("RefDimension2").isEmpty()) {
                tag.putLong("RefPos2", pos.asLong());
                tag.putString("RefDimension2", world.dimension().location().toString());
            }
            else {
                tag.putLong("RefPos1", pos.asLong());
                tag.putString("RefDimension1", world.dimension().location().toString());
                tag.putLong("RefPos2", 0L);
                tag.putString("RefDimension2", "");
            }
            return ActionResultType.SUCCESS;
        }

        if (world.getBlockState(pos).getBlock() == PtBlocks.MIRROR.get()) {

            String dimension1 = tag.getString("RefDimension1");
            String dimension2 = tag.getString("RefDimension2");

            if (dimension1.isEmpty() || dimension2.isEmpty() || !dimension1.equals(dimension2)) {
                return ActionResultType.SUCCESS;
            }

            long refPos1 = tag.getLong("RefPos1");
            long refPos2 = tag.getLong("RefPos2");
            PtMirrorTile mirrorTile = (PtMirrorTile) world.getBlockEntity(pos);
            if (mirrorTile != null) {
                mirrorTile.setFacingVector(BlockPos.of(refPos1), BlockPos.of(refPos2));
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }



}
