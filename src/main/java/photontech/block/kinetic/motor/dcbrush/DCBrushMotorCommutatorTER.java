package photontech.block.kinetic.motor.dcbrush;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import photontech.utils.PtConstants;
import photontech.utils.client.render.KineticMachineTER;
import photontech.utils.client.render.SuperByteBuffer;
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nonnull;

public class DCBrushMotorCommutatorTER extends KineticMachineTER<DCBrushMotorCommutatorTile> {

    public DCBrushMotorCommutatorTER(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(@Nonnull DCBrushMotorCommutatorTile machine, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        super.render(machine, partialTicks, matrixStack, bufferIn, combinedLightIn, combinedOverlayIn);
        Direction.Axis brushAxis = machine.getBrushAxis();
        if (brushAxis != null) {
            SuperByteBuffer superByteBuffer = bufferFromResourceLocation(machine.getBlockState(), PtConstants.MODELS.BRUSH_MODEL);
            rotateBuffer(superByteBuffer, AxisHelper.getVerticalAxis(machine.getBrushAxis(), Direction.Axis.Y), HALF_PI, combinedLightIn);
            for (RenderType type : RenderType.chunkBufferLayers()) {
                superByteBuffer.renderInto(matrixStack, bufferIn.getBuffer(type));
            }
        }

    }

}
