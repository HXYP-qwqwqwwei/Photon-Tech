package photontech.block.crucible;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import photontech.init.PtTileEntities;
import photontech.utils.helper.CrucibleConnections;
import photontech.utils.PtConstants;
import photontech.utils.Utils;
import photontech.utils.capability.fluid.IMultiFluidTank;
import photontech.utils.capability.fluid.PtMultiFluidTank;
import photontech.utils.capability.heat.PtHeatCache;
import photontech.utils.recipe.PtConditionalRecipe;
import photontech.utils.tileentity.IHeatReservoirTile;
import photontech.utils.tileentity.PtMachineTile;
import net.minecraft.tileentity.ITickableTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class PtCrucibleTileEntity extends PtMachineTile implements ITickableTileEntity, INamedContainerProvider, IHeatReservoirTile {

    private enum RecipeMode {
        MELTING(0), COOLING(1), OTHER(2);

        final int cacheListIndex;

        RecipeMode(int index) {
            this.cacheListIndex = index;
        }

        int getIndex() {
            return cacheListIndex;
        }
    }

    private static final int maxFilling = 16;
    
    public PtCrucibleTileEntity(float overloadTemp, float heatTransferRate) {
        super(PtTileEntities.CRUCIBLE_TILEENTITY.get());

        this.mainItemHandler = LazyOptional.of(() -> this.createItemHandler(10));
        this.fluidTanks = LazyOptional.of(() -> this.createFluidTanks(8, 16000));
        // 输入输出栏和合成栏初始化
        this.itemIOHandler = LazyOptional.of(() -> this.createIOLimitedHandler(slot -> slot < 9, slot -> slot < 9));
        this.recipeIOHandler = LazyOptional.of(() -> this.createIOLimitedHandler(slot -> true, slot -> true));
        // 热容初始化
        this.heatReservoir = LazyOptional.of(() -> this.createHeatReservoir(ENVIRONMENT_TEMPERATURE, overloadTemp, 500, heatTransferRate));

        cachedRecipes = Arrays.asList(null, null, null);
        heatCaches = NonNullList.create();
        for (int i = 0; i < 3; ++i) {
            PtHeatCache heatCache = new PtHeatCache();
            heatCache.init(ENVIRONMENT_TEMPERATURE, 0);
            heatCaches.add(heatCache);
        }
    }


    public void transferLiquidToNearByCrucible(TileEntity tileEntity) {
        LazyOptional<PtMultiFluidTank> targetFluidTank = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).cast();
        targetFluidTank.ifPresent(tTank -> IMultiFluidTank.fluidExchange(this.getFluidTanks(), tTank));
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {
            pullItemFromEntity();
            Direction[] directions = Direction.values();
            for (Direction direction : directions) {
                heatExchangeWithTile(this.getHeatReservoir(), this.level.getBlockEntity(this.worldPosition.relative(direction)), direction.getOpposite());
            }
            // 向环境散热
            this.heatExchangeWithEnvironment(this.getHeatReservoir());
            // 尝试从篝火中获取热量
            this.heatExchangeWithCampfireHeat(this.level, this.worldPosition, this.getHeatReservoir());
            // 尝试从雨中接水
            this.tryAcceptWaterFromRain();

            this.startHeatProcess("melting", RecipeMode.MELTING);
            this.startHeatProcess("cooling", RecipeMode.COOLING);
            this.startHeatProcess("other", RecipeMode.OTHER);

            CrucibleConnections crucible = CrucibleConnections.getInstance(this.getBlockState());
            assert crucible != null;

            for (Direction direction : directions) {
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    continue;
                }
                if (crucible.connectedTo(direction)) {
                    TileEntity tileEntity = level.getBlockEntity(this.worldPosition.relative(direction));
                    if (tileEntity instanceof PtCrucibleTileEntity) {
                        this.transferLiquidToNearByCrucible(tileEntity);
                    }
                }
            }

            level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);


        }
    }

    private void startHeatProcess(String group, RecipeMode mode) {

        this.updateCachedRecipe(group, mode.getIndex(), 0, 8, 9, 0, 8, PtConstants.NATURAL_HEAT_RECIPE_COMPARATOR);

        if (!this.startHeatRecipeProcess(this.cachedRecipes.get(mode.getIndex()), this.heatCaches.get(mode.getIndex()))) {
            if (this.handleRecipeInput(this.cachedRecipes.get(mode.getIndex()), 0, 8, 9, 0, 8)) {
                this.handleRecipeResult(this.cachedRecipes.get(mode.getIndex()));
            }
        }
    }


    private void tryAcceptWaterFromRain() {
        assert this.level != null;
        if (this.level.isRainingAt(this.worldPosition.above())) {
            this.getFluidTanks().fill(new FluidStack(Fluids.WATER, 1), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private void pullItemFromEntity() {
        assert level != null;
        List<ItemEntity> itemEntities = level.getEntities(EntityType.ITEM, new AxisAlignedBB(worldPosition, worldPosition.offset(1, 1, 1)), (itemEntity) -> true);
        for (ItemEntity entity : itemEntities) {
            if (!ItemHandlerHelper.insertItem(this.getItemIOHandler(), entity.getItem(), true).equals(entity.getItem(), false)) {
                entity.setItem(ItemHandlerHelper.insertItem(this.getItemIOHandler(), entity.getItem(), false));
            }
        }
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        super.save(nbt);

        // CACHES
        saveCachedRecipe(nbt, this.cachedRecipes.get(RecipeMode.MELTING.getIndex()), "CachedMeltingRecipe");
        saveCachedRecipe(nbt, this.cachedRecipes.get(RecipeMode.COOLING.getIndex()), "CachedCoolingRecipe");
        saveCachedRecipe(nbt, this.cachedRecipes.get(RecipeMode.OTHER.getIndex()), "CachedOtherRecipe");
        nbt.put("HeatCacheForMelting", this.heatCaches.get(RecipeMode.MELTING.getIndex()).save(new CompoundNBT()));
        nbt.put("HeatCacheForCooling", this.heatCaches.get(RecipeMode.COOLING.getIndex()).save(new CompoundNBT()));
        nbt.put("HeatCacheForOther", this.heatCaches.get(RecipeMode.OTHER.getIndex()).save(new CompoundNBT()));

        // MAIN
        this.heatReservoir.ifPresent(reservoir -> nbt.put("HeatReservoir", reservoir.save(new CompoundNBT())));
        return nbt;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        // CACHES
        this.cachedRecipes.set(RecipeMode.MELTING.getIndex(), PtConditionalRecipe.loadFromNBT(nbt.getCompound("CachedMeltingRecipe")));
        this.cachedRecipes.set(RecipeMode.COOLING.getIndex(), PtConditionalRecipe.loadFromNBT(nbt.getCompound("CachedCoolingRecipe")));
        this.cachedRecipes.set(RecipeMode.OTHER.getIndex(), PtConditionalRecipe.loadFromNBT(nbt.getCompound("CachedOtherRecipe")));
        this.heatCaches.get(RecipeMode.MELTING.getIndex()).load(nbt.getCompound("HeatCacheForMelting"));
        this.heatCaches.get(RecipeMode.COOLING.getIndex()).load(nbt.getCompound("HeatCacheForCooling"));
        this.heatCaches.get(RecipeMode.OTHER.getIndex()).load(nbt.getCompound("HeatCacheForOther"));

    }

    public int getMaxFilling() {
        return PtCrucibleTileEntity.maxFilling;
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui." + Utils.MOD_ID + ".crucible");
    }

    @Nullable
    @Override
    public Container createMenu(int sysID, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player) {
        assert this.level != null;
        return new PtCrucibleContainer(sysID, inventory, this.worldPosition, this.level);
    }


    public PtHeatCache getHeatCacheForMelting() {
        return heatCaches.get(RecipeMode.MELTING.getIndex());
    }

    public PtHeatCache getHeatCacheForCooling() {
        return heatCaches.get(RecipeMode.COOLING.getIndex());
    }

    public PtHeatCache getHeatCacheForOther() {
        return heatCaches.get(RecipeMode.OTHER.getIndex());
    }

}
