package photontech.block.kinetic.axle;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import photontech.utils.PtConstants;
import photontech.utils.render.PtModelRenderer;

import javax.annotation.Nonnull;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class AxleTER extends TileEntityRenderer<AxleTile> {

    public AxleTER(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(@Nonnull AxleTile axleTile, float partialTicks, MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        PtModelRenderer modelRenderer = PtModelRenderer.create(blockRenderer, bufferIn, axleTile.getBlockState(), combinedLightIn, combinedOverlayIn);
        ModelManager manager = Minecraft.getInstance().getModelManager();


        matrixStack.pushPose();
        Direction.Axis axis = axleTile.getBlockState().getValue(AXIS);

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
        float angle = axleTile.getAngle();
        matrixStack.mulPose(Vector3f.XP.rotation(angle));
        matrixStack.translate(-0.5F, -0.5F, -0.5F);

        // render model
        IBakedModel axleModel = manager.getModel(PtConstants.MODELS.IRON_AXLE_MODEL);
        modelRenderer.renderModel(matrixStack, axleModel, RenderType.solid());

        matrixStack.popPose();
    }

}


