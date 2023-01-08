package photontech.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;

public interface IPartalBlock {

    VoxelShape getShape(BlockState state);
}
