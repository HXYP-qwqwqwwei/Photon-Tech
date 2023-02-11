package photontech.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;

public interface PartialBlock {

    VoxelShape getShape(BlockState state);
}
