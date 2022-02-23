package photontech.block.heater;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import photontech.utils.capability.heat.PtBurningHeatCache;
import photontech.utils.capability.heat.PtHeatCache;
import photontech.utils.recipe.PtConditionalRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;

public abstract class PtBurningHeaterTile extends PtHeaterTile {

    protected boolean isIgnited = false;

    public PtBurningHeaterTile(TileEntityType<?> tileEntityTypeIn, int slots, int tankCapacity) {
        super(tileEntityTypeIn, Direction.UP);
        if (slots > 0) {
            this.mainItemHandler = LazyOptional.of(() -> this.createItemHandler(slots));
        }
        if (tankCapacity > 0) {
            this.fluidTanks = LazyOptional.of(() -> this.createFluidTanks(1, tankCapacity));
        }

        // recipe
        this.cachedRecipes = new ArrayList<>();
        this.cachedRecipes.add(null);
        this.heatCaches = NonNullList.create();
        this.heatCaches.add(new PtBurningHeatCache());
    }


    protected void startBurningRecipe(int inputSlotBegin, int inputSlotEnd, int slotCatalyst, int inputTankBegin, int inputTankEnd, @Nullable Comparator<PtConditionalRecipe> comparator) {
        if (!this.isIgnited) {
            this.heatCaches.get(0).init(1, 0);
            this.cachedRecipes.set(0, null);
            return;
        }
        if (!startHeatRecipeProcess(this.cachedRecipes.get(0), this.heatCaches.get(0))) {
            this.handleRecipeResult(this.cachedRecipes.get(0));
            this.updateCachedRecipe("burning", 0, inputSlotBegin, inputSlotEnd, slotCatalyst, inputTankBegin, inputTankEnd, comparator);
            this.handleRecipeInput(this.cachedRecipes.get(0), inputSlotBegin, inputSlotEnd, slotCatalyst, inputTankBegin, inputTankEnd);
        }
    }


    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        this.cachedRecipes.set(0, PtConditionalRecipe.loadFromNBT(nbt.getCompound("CachedBurningRecipe")));
        this.heatCaches.get(0).load(nbt.getCompound("HeatCacheForBurning"));
        this.isIgnited = nbt.getBoolean("IsIgnited");
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);
        saveCachedRecipe(nbt, this.cachedRecipes.get(0), "CachedBurningRecipe");
        nbt.put("HeatCacheForBurning", this.heatCaches.get(0).save(new CompoundNBT()));
        nbt.putBoolean("IsIgnited", this.isIgnited);
        return nbt;
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
