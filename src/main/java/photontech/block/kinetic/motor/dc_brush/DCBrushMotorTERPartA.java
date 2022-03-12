package photontech.block.kinetic.motor.dc_brush;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import photontech.utils.PtConstants;
import photontech.utils.client.render.KtMachineTER;
import photontech.utils.client.render.SuperByteBuffer;

import javax.annotation.Nonnull;

public class DCBrushMotorTERPartA extends KtMachineTER<DCBrushTilePartA> {

    public DCBrushMotorTERPartA(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(@Nonnull DCBrushTilePartA partA, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(partA, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
        Direction.Axis brushAxis = partA.getBrushAxis();
        if (brushAxis != null) {
            SuperByteBuffer superByteBuffer = this.createBufferFromModel(partA.getBlockState(), this.getBrushModelId(brushAxis));
            for (RenderType type : RenderType.chunkBufferLayers()) {
                renderStaticBuffer(superByteBuffer, matrixStack, bufferIn.getBuffer(type), combinedLightIn);
            }

        }

    }

    protected ResourceLocation getBrushModelId(Direction.Axis axis) {
        return PtConstants.MODELS.DC_BRUSH_MODELS[axis.ordinal()];
    }
}
