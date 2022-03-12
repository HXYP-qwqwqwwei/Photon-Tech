package photontech.block.heater.solid;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import photontech.PhotonTech;
import photontech.block.heater.PtBurningHeaterTile;
import photontech.init.PtTileEntities;

import static net.minecraft.state.properties.BlockStateProperties.*;
import static photontech.utils.PtConstants.BlockStateProperties.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PtBurningItemHeaterTile extends PtBurningHeaterTile implements INamedContainerProvider {


    public PtBurningItemHeaterTile() {
        super(PtTileEntities.HEATER_TILEENTITY.get(), 2, 0);
        this.itemIOHandler = LazyOptional.of(() -> this.createIOLimitedHandler(slot -> slot == 0, slot -> slot == 1));
        this.recipeIOHandler = LazyOptional.of(() -> this.createIOLimitedHandler(slot -> slot == 1, slot -> slot == 0));
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {

            this.startBurningRecipe(0, 1, -1, 0, 0, null);
            this.heatExchangeWithEnvironment(this.getHeatReservoir());

            BlockState heaterBlock = level.getBlockState(this.worldPosition);

            this.isIgnited = this.getHeatCacheForBurning().isInProcess();

            if (this.isIgnited != this.getBlockState().getValue(LIT) || this.isFuelIn() != this.getBlockState().getValue(HOLDING_INPUT)) {
                level.setBlock(
                        this.worldPosition,
                        heaterBlock.setValue(LIT, this.isIgnited).setValue(HOLDING_INPUT, this.isFuelIn()),
                        1
                );

                level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
            }

        }
    }


    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui." + PhotonTech.ID + ".burning_item_heater");
    }

    @Nullable
    @Override
    public Container createMenu(int sysID, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player) {
        assert this.level != null;
        return new PtBurningItemHeaterContainer(sysID, inventory, this.worldPosition, this.level);
    }

    public boolean isFuelIn() {
        return this.getItemHandler().getStackInSlot(0) != ItemStack.EMPTY;
    }

}
