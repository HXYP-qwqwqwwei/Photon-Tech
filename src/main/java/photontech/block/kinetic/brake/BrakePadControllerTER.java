package photontech.block.kinetic.brake;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import photontech.init.PtBlocks;
import photontech.utils.client.render.MachineTileRenderer;
import photontech.utils.client.render.SuperByteBuffer;

import javax.annotation.Nonnull;

import static net.minecraft.state.properties.BlockStateProperties.FACING;
import static photontech.utils.PtConstants.BlockStateProperties.AXIS_ROTATED;
import static photontech.utils.PtConstants.MODELS.*;

public class BrakePadControllerTER extends MachineTileRenderer<BrakePadControllerTile> {
    public BrakePadControllerTER(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(@Nonnull BrakePadControllerTile machine, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction backSide = machine.getBackSide();
        for (RenderType type : RenderType.chunkBufferLayers()) {
            if (RenderTypeLookup.canRenderInLayer(machine.getBlockState(), type)) {
                if (machine.isConnected()) {
                    BlockState pipeState = PtBlocks.HYDRAULIC_PIPE.get().defaultBlockState().setValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(backSide), true);
                    SuperByteBuffer pipeBuffer = bufferFromBlockState(pipeState);
                    pipeBuffer.light(combinedLightIn).renderInto(matrixStack, bufferIn.getBuffer(type));
                }
                BlockState state = machine.getBlockState();
                ResourceLocation padModelRL;

                if (state.getValue(AXIS_ROTATED)) {
                    padModelRL = machine.isPushed() ? BRAKE_PAD_PUSHED_VERTICAL : BRAKE_PAD_VERTICAL;
                } else padModelRL = machine.isPushed() ? BRAKE_PAD_PUSHED : BRAKE_PAD;

                SuperByteBuffer padBuffer = bufferFromResourceLocation(state, padModelRL);
                transformByFacing(padBuffer, state.getValue(FACING), combinedLightIn);
                padBuffer.renderInto(matrixStack, bufferIn.getBuffer(type));
            }
        }
    }

}
