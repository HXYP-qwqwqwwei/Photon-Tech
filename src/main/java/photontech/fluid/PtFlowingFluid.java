//package photontech.fluid;
//
//import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
//import net.minecraft.block.*;
//import net.minecraft.block.material.Material;
//import net.minecraft.fluid.FlowingFluid;
//import net.minecraft.fluid.Fluid;
//import net.minecraft.fluid.FluidState;
//import net.minecraft.fluid.WaterFluid;
//import net.minecraft.item.Item;
//import net.minecraft.tags.BlockTags;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.IWorldReader;
//import net.minecraftforge.fluids.ForgeFlowingFluid;
//
//import java.lang.reflect.Method;
//import java.util.Map;
//
//public abstract class PtFlowingFluid extends ForgeFlowingFluid {
//
//    public PtFlowingFluid(Properties properties) {
//        super(properties);
//    }
//
//    public static class Source extends ForgeFlowingFluid.Source {
//
//        public Source(Properties properties) {
//            super(properties);
//        }
//
//        @Override
//        protected void spread(IWorld world, BlockPos pos, FluidState fluidState) {
//            if (!fluidState.isEmpty()) {
//                BlockState blockstate = world.getBlockState(pos);
////            BlockPos blockpos = pos.below();
//                BlockPos blockpos = pos.above();
//                BlockState blockstate1 = world.getBlockState(blockpos);
//                FluidState fluidstate = this.getNewLiquid(world, blockpos, blockstate1);
//                if (this.canSpreadTo(world, pos, blockstate, Direction.DOWN, blockpos, blockstate1, world.getFluidState(blockpos), fluidstate.getType())) {
//                    this.spreadTo(world, blockpos, blockstate1, Direction.DOWN, fluidstate);
//                    if (this.sourceNeighborCount(world, pos) >= 3) {
//                        this.spreadToSides(world, pos, fluidState, blockstate);
//                    }
//                } else if (fluidState.isSource() || !this.isWaterHole(world, fluidstate.getType(), pos, blockstate, blockpos, blockstate1)) {
//                    this.spreadToSides(world, pos, fluidState, blockstate);
//                }
//
//            }
//        }
//    }
//
//    public static class Flowing extends ForgeFlowingFluid.Flowing {
//
//        public Flowing(Properties properties) {
//            super(properties);
//        }
//
//        @Override
//        protected void spread(IWorld world, BlockPos pos, FluidState fluidState) {
//            if (!fluidState.isEmpty()) {
//                BlockState blockstate = world.getBlockState(pos);
////            BlockPos blockpos = pos.below();
//                BlockPos blockpos = pos.above();
//                BlockState blockstate1 = world.getBlockState(blockpos);
//                FluidState fluidstate = this.getNewLiquid(world, blockpos, blockstate1);
//                if (this.canSpreadTo(world, pos, blockstate, Direction.DOWN, blockpos, blockstate1, world.getFluidState(blockpos), fluidstate.getType())) {
//                    this.spreadTo(world, blockpos, blockstate1, Direction.DOWN, fluidstate);
//                    if (this.sourceNeighborCount(world, pos) >= 3) {
//                        this.spreadToSides(world, pos, fluidState, blockstate);
//                    }
//                } else if (fluidState.isSource() || !this.isWaterHole(world, fluidstate.getType(), pos, blockstate, blockpos, blockstate1)) {
//                    this.spreadToSides(world, pos, fluidState, blockstate);
//                }
//
//            }
//        }
//    }
//
//
//
////    @Override
////    public boolean isSource(FluidState p_207193_1_) {
////        return false;
////    }
////
////    @Override
////    public int getAmount(FluidState p_207192_1_) {
////        return 0;
////    }
//}
