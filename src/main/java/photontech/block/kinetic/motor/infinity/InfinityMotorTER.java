package photontech.block.kinetic.motor.infinity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import photontech.utils.PtConstants;
import photontech.utils.client.render.KtMachineTER;
import photontech.utils.client.render.SuperByteBuffer;
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nonnull;

public class InfinityMotorTER extends KtMachineTER<InfinityMotorTile> {
    public InfinityMotorTER(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(@Nonnull InfinityMotorTile te, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        for (RenderType type : RenderType.chunkBufferLayers()) {
            SuperByteBuffer superByteBuffer = getModel(te.getAxleBlockState(), PtConstants.MODELS.INFINITY_MOTOR_ROTATER);
            if (RenderTypeLookup.canRenderInLayer(te.getBlockState(), type)){

                if (te.getAxleBlockState() != null) {
                    renderRotatingBuffer(te, getBlockModel(te.getAxleBlockState()), matrixStack, bufferIn.getBuffer(type), combinedLightIn);
                }

                SuperByteBuffer rotatedBuffer = standardKineticRotationTransform(superByteBuffer, te, combinedLightIn);
                SuperByteBuffer fixedBuffer = kineticRotationTransform(rotatedBuffer,  AxisHelper.getVerticalAxis(te.getAxis(), Direction.Axis.Y), HALF_PI, combinedLightIn);
                fixedBuffer.renderInto(matrixStack, bufferIn.getBuffer(type));

            }
        }
    }
}
