//package photontech.mixins;
//
//import com.google.common.collect.Lists;
//import net.minecraft.block.BlockState;
//import net.minecraft.crash.CrashReport;
//import net.minecraft.crash.CrashReportCategory;
//import net.minecraft.crash.ReportedException;
//import net.minecraft.profiler.IProfiler;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.World;
//import net.minecraftforge.common.capabilities.CapabilityProvider;
//import net.minecraftforge.common.extensions.IForgeWorld;
//import org.apache.logging.log4j.LogManager;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//import photontech.utils.tileentity.MachineTile;
//
//import javax.annotation.Nonnull;
//import java.util.*;
//import java.util.function.Consumer;
//
//@SuppressWarnings("SpellCheckingInspection")
//@Mixin(value = World.class)
//public abstract class MixinWorld extends CapabilityProvider<World> implements IWorld, AutoCloseable, IForgeWorld {
//
//    @Shadow protected boolean updatingBlockEntities;
//    @Shadow @Final public boolean isClientSide;
//    @Nonnull
//    @Shadow public abstract BlockState getBlockState(@Nonnull BlockPos p_180495_1_);
//
//    @Shadow public abstract void removeBlockEntity(BlockPos p_175713_1_);
//
//    public final List<MachineTile> runingMachines = Lists.newArrayList();
//    public final Set<MachineTile> runingSet = Collections.newSetFromMap(new IdentityHashMap<>());
//    protected final Set<MachineTile> machinesToUnload = Collections.newSetFromMap(new IdentityHashMap<>());
//
//    protected MixinWorld(Class<World> baseClass) {
//        super(baseClass);
//    }
//
//    @Redirect(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Ljava/util/Set;forEach(Ljava/util/function/Consumer;)V"))
//    private void forEachProxy(Set<TileEntity> entitiesToUnload, Consumer<? super TileEntity> consumer) {
//        entitiesToUnload.forEach(te -> {
//            if (te instanceof MachineTile && ((MachineTile) te).isPrimary()) {
//                machinesToUnload.add((MachineTile) te);
//                runingSet.remove(te);
//            }
//            te.onChunkUnloaded();
//        });
//    }
//
//    @Inject(method = "addBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2, shift = At.Shift.AFTER))
//    protected void onAddBlockEntity(TileEntity tileEntity, CallbackInfoReturnable<Boolean> cir) {
//        if (tileEntity instanceof MachineTile && ((MachineTile) tileEntity).isPrimary() && !runingSet.contains(tileEntity)) {
//            this.runingMachines.add((MachineTile) tileEntity);
//            runingSet.add((MachineTile) tileEntity);
//        }
//    }
//
//    @Inject(method = "removeBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", ordinal = 4, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
//    protected void onRemoveBlockEntity(BlockPos blockPos, CallbackInfo ci, TileEntity tileentity) {
//        if (tileentity instanceof MachineTile) {
//            runingMachines.remove(tileentity);
//            runingSet.remove(tileentity);
//        }
//    }
//
//
//    @Inject(method = "tickBlockEntities", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/tileentity/ITickableTileEntity;tick()V"), locals = LocalCapture.CAPTURE_FAILHARD)
//    protected void onTickBlockEntities(CallbackInfo ci, IProfiler iProfiler, Iterator<TileEntity> iterator, TileEntity tileEntity, BlockPos blockPos) {
//        if (tileEntity instanceof MachineTile) {
//            MachineTile machine = (MachineTile) tileEntity;
//            if (machine.isPrimary() && !runingSet.contains(machine)) {
//                runingMachines.add(machine);
//                runingSet.add(machine);
//            }
//        }
//    }
//
//
//
//    @Inject(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/IProfiler;popPush(Ljava/lang/String;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
//    protected void onTickBlockEntities(CallbackInfo ci, IProfiler iprofiler) {
//        iprofiler.popPush("runingMachines");
//        this.updatingBlockEntities = true;// Forge: Move above remove to prevent CMEs
//        if (!this.machinesToUnload.isEmpty()) {
//            this.runingMachines.removeAll(this.machinesToUnload);
//            this.machinesToUnload.clear();
//        }
//
//        Iterator<MachineTile> iterator = this.runingMachines.iterator();
//
//        while(iterator.hasNext()) {
//            MachineTile machine = iterator.next();
//
//            if (!machine.isRemoved() && machine.hasLevel() && machine.isPrimary()) {
//                BlockPos blockpos = machine.getBlockPos();
//                if (this.getChunkSource().isTickingChunk(blockpos) && this.getWorldBorder().isWithinBounds(blockpos)) {
//                    try {
//                        net.minecraftforge.server.timings.TimeTracker.TILE_ENTITY_UPDATE.trackStart(machine);
//                        iprofiler.push(() -> String.valueOf(machine.getType().getRegistryName()));
//                        if (machine.getType().isValid(this.getBlockState(blockpos).getBlock())) {
//                            machine.run();
//                        } else {
//                            machine.logInvalidState();
//                        }
//
//                        iprofiler.pop();
//                    } catch (Throwable throwable) {
//                        CrashReport crashreport = CrashReport.forThrowable(throwable, "Runing Machine");
//                        CrashReportCategory crashreportcategory = crashreport.addCategory("Machine being ran");
//                        machine.fillCrashReportCategory(crashreportcategory);
//                        if (net.minecraftforge.common.ForgeConfig.SERVER.removeErroringTileEntities.get()) {
//                            LogManager.getLogger().fatal("{}", crashreport.getFriendlyReport());
//                            machine.setRemoved();
//                            this.removeBlockEntity(machine.getBlockPos());
//                        } else
//                            throw new ReportedException(crashreport);
//                    }
//                    finally {
//                        net.minecraftforge.server.timings.TimeTracker.TILE_ENTITY_UPDATE.trackEnd(machine);
//                    }
//                }
//            }
//
//            if (machine.isRemoved() || !machine.isPrimary()) {
//                iterator.remove();
//                runingSet.remove(machine);
//            }
//        }
//
//        this.updatingBlockEntities = false;
//    }
//
//
//}
