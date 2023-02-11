package photontech.block.kinetic.motor.infinity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import photontech.utils.PtConstants;
import photontech.utils.client.render.KineticMachineTER;
import photontech.utils.client.render.SuperByteBuffer;
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nonnull;

public class InfinityMotorTER extends KineticMachineTER<InfinityMotorTile> {
    public InfinityMotorTER(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(@Nonnull InfinityMotorTile machine, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        SuperByteBuffer superByteBuffer = bufferFromResourceLocation(machine.getAxleBlockState(), PtConstants.MODELS.INFINITY_MOTOR_ROTATER);
        for (RenderType type : RenderType.chunkBufferLayers()) {
            if (RenderTypeLookup.canRenderInLayer(machine.getBlockState(), type)){

                if (machine.getAxleBlockState() != null) {
                    renderRotatingBuffer(machine, bufferFromBlockState(machine.getAxleBlockState()), matrixStack, bufferIn.getBuffer(type), combinedLightIn);
                }

                rotateBuffer(superByteBuffer, machine.getAxis(), machine.getAngle(), combinedLightIn);
                rotateBuffer(superByteBuffer,  AxisHelper.getVerticalAxis(machine.getAxis(), Direction.Axis.Y), HALF_PI, combinedLightIn);
                superByteBuffer.renderInto(matrixStack, bufferIn.getBuffer(type));

            }
        }
    }
}
