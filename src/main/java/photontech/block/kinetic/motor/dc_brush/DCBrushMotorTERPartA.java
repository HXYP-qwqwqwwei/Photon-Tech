package photontech.block.kinetic.motor.dc_brush;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import photontech.utils.PtConstants;
import photontech.utils.client.render.KtMachineTER;
import photontech.utils.client.render.SuperByteBuffer;
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nonnull;

public class DCBrushMotorTERPartA extends KtMachineTER<DCBrushTilePartA> {

    public DCBrushMotorTERPartA(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(@Nonnull DCBrushTilePartA partA, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(partA, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
        Direction.Axis brushAxis = partA.getBrushAxis();
        if (brushAxis != null) {
            SuperByteBuffer superByteBuffer = this.getModel(partA.getBlockState(), PtConstants.MODELS.DC_BRUSH_MODEL);
            for (RenderType type : RenderType.chunkBufferLayers()) {
                kineticRotationTransform(superByteBuffer, AxisHelper.getVerticalAxis(brushAxis, Direction.Axis.Y), HALF_PI, combinedLightIn).renderInto(matrixStack, bufferIn.getBuffer(type));
            }
        }

    }


}
