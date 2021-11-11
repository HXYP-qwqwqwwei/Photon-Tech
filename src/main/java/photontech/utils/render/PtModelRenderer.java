package photontech.utils.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraftforge.client.model.data.EmptyModelData;

public class PtModelRenderer {

    private final BlockRendererDispatcher blockRenderer;
    private final IRenderTypeBuffer buffer;
    private final BlockState blockState;
    private final int combinedLightIn;
    private final int combinedOverlayIn;

    public static PtModelRenderer create(BlockRendererDispatcher blockRenderer, IRenderTypeBuffer buffer, BlockState blockState, int combinedLightIn, int combinedOverlayIn) {
        return new PtModelRenderer(blockRenderer, buffer, blockState, combinedLightIn, combinedOverlayIn);
    }

    private PtModelRenderer(BlockRendererDispatcher blockRenderer, IRenderTypeBuffer buffer, BlockState blockState, int combinedLightIn, int combinedOverlayIn) {
        this.blockRenderer = blockRenderer;
        this.buffer = buffer;
        this.blockState = blockState;
        this.combinedLightIn = combinedLightIn;
        this.combinedOverlayIn = combinedOverlayIn;
    }


    public void renderModel(MatrixStack matrixStack, IBakedModel model, RenderType type) {
        blockRenderer.getModelRenderer().renderModel(
                matrixStack.last(),
                buffer.getBuffer(type),
                blockState,
                model,
                1F, 1F, 1F,
                combinedLightIn, combinedOverlayIn,
                EmptyModelData.INSTANCE
        );
    }
}
