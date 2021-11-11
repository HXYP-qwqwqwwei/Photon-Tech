package photontech.block.kinetic.motor.dc_brush;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import photontech.utils.PtConstants;
import photontech.utils.helper.AxisHelper;
import photontech.utils.render.PtModelRenderer;

import javax.annotation.Nonnull;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;

public class DCBrushMotorTERPartA extends TileEntityRenderer<DCBrushTilePartA> {

    public DCBrushMotorTERPartA(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(@Nonnull DCBrushTilePartA motorTile, float partialTicks, MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        PtModelRenderer modelRenderer = PtModelRenderer.create(blockRenderer, bufferIn, motorTile.getBlockState(), combinedLightIn, combinedOverlayIn);
        ModelManager manager = Minecraft.getInstance().getModelManager();

        matrixStack.pushPose();
        Direction.Axis axis = motorTile.getBlockState().getValue(AXIS);

        matrixStack.translate(0.5F, 0.5F, 0.5F);
        switch (axis) {
            case Y:
                matrixStack.mulPose(Vector3f.ZP.rotation((float) (Math.PI * 0.5)));
                break;
            case Z:
                matrixStack.mulPose(Vector3f.YP.rotation((float) (Math.PI * 0.5)));
                break;
            default:
                break;
        }
        float angle = motorTile.getAngle(AxisHelper.getAxisPositiveDirection(axis));
        matrixStack.mulPose(Vector3f.XP.rotation(angle));
        matrixStack.translate(-0.5F, -0.5F, -0.5F);

        // render model
        modelRenderer.renderModel(matrixStack, manager.getModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_A_CONTACTOR), RenderType.solid());
        for (int i = 0; i < 8; ++i) {
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(Vector3f.XP.rotation((float) (Math.PI * 0.25)));
            matrixStack.translate(-0.5F, -0.5F, -0.5F);
            modelRenderer.renderModel(matrixStack, manager.getModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_A_WIRES), RenderType.solid());

        }

        matrixStack.popPose();
    }
}
