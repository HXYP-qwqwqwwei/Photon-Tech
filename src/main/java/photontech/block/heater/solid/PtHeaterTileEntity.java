package photontech.block.heater.solid;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import photontech.init.PtCapabilities;
import photontech.init.PtTileEntities;
import photontech.utils.PtConstants;
import photontech.utils.Utils;
import photontech.utils.capability.heat.PtBurningHeatCache;
import photontech.utils.capability.heat.PtHeatCache;
import photontech.utils.recipe.PtConditionalRecipe;
import photontech.utils.tileentity.PtMachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class PtHeaterTileEntity extends PtMachineTile implements INamedContainerProvider {

    private boolean isIgnited = false;

    public PtHeaterTileEntity() {
        super(PtTileEntities.HEATER_TILEENTITY.get());
        this.heatReservoir = LazyOptional.of(() -> this.createHeatReservoir(ENVIRONMENT_TEMPERATURE, 10000, 100, 100F));
        this.mainItemHandler = LazyOptional.of(() -> this.createItemHandler(2));
        this.itemIOHandler = LazyOptional.of(() -> this.createIOLimitedHandler(slot -> slot == 0, slot -> slot == 1));
        this.recipeIOHandler = LazyOptional.of(() -> this.createIOLimitedHandler(slot -> slot == 1, slot -> slot == 0));
        this.cachedRecipes = new ArrayList<>();
        cachedRecipes.add(null);
        this.heatCaches = NonNullList.create();
        heatCaches.add(new PtBurningHeatCache());
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {

            this.startBurningRecipe();
            this.heatExchangeWithEnvironment(this.getHeatReservoir());

            BlockState heaterBlock = level.getBlockState(this.worldPosition);

            this.isIgnited = this.getHeatCacheForBurning().isInProcess();

            level.setBlock(
                    this.worldPosition,
                    heaterBlock.setValue(BlockStateProperties.LIT, this.isIgnited).setValue(PtConstants.HOLDING_INPUT, this.isFuelIn()),
                    1
            );

            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    private void startBurningRecipe() {
        if (!this.isIgnited) {
            this.heatCaches.get(0).init(1, 0);
            this.cachedRecipes.set(0, null);
            return;
        }
        if (!startHeatRecipeProcess(this.cachedRecipes.get(0), this.heatCaches.get(0))) {
//            this.isIgnited = true;
            this.handleRecipeResult(this.cachedRecipes.get(0));
            this.updateCachedRecipe("burning", 0, 0, 1, 0, 0, null);
            this.handleRecipeInput(this.cachedRecipes.get(0), 0, 1, 0, 0);
        }
    }



    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == PtCapabilities.HEAT_RESERVOIR) {
            if (side == Direction.UP) {
                return super.getCapability(cap, side);
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.cachedRecipes.set(0, PtConditionalRecipe.loadFromNBT(nbt.getCompound("CachedBurningRecipe")));
        this.heatCaches.get(0).loadFromNBT(nbt.getCompound("HeatCacheForBurning"));
        this.isIgnited = nbt.getBoolean("IsIgnited");
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        saveCachedRecipe(nbt, this.cachedRecipes.get(0), "CachedBurningRecipe");
        nbt.put("HeatCacheForBurning", this.heatCaches.get(0).saveToNBT(new CompoundNBT()));
        nbt.putBoolean("IsIgnited", this.isIgnited);
        return nbt;
    }


    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui." + Utils.MOD_ID + ".heater");
    }

    @Nullable
    @Override
    public Container createMenu(int sysID, PlayerInventory inventory, PlayerEntity player) {
        assert this.level != null;
        return new PtHeaterContainer(sysID, inventory, this.worldPosition, this.level);
    }

    public PtHeatCache getHeatCacheForBurning() {
        return this.heatCaches.get(0);
    }

    public void setIgnited(boolean isIgnited) {
        this.isIgnited = isIgnited;
    }

    public boolean isIgnited() {
        return this.isIgnited;
    }
}
