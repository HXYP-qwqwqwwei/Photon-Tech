package photontech.block.multipart;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;
import photontech.block.IPartalBlock;

public interface IMultiPartTile {

    @SuppressWarnings("all")
    static <T extends IPartalBlock> VoxelShape getShape(BlockPart<T> part, BlockState state) {
        T block = (T) state.getBlock();
        return block.getShape(state);
    }
}
