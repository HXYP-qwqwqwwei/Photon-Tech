package photontech.block.mirror;

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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import photontech.utils.PtConstants;
import photontech.utils.client.render.PtModelRenderer;

import static java.lang.Math.*;
import static net.minecraft.state.properties.BlockStateProperties.*;

public class PtMirrorTER extends TileEntityRenderer<PtMirrorTile> {

    public PtMirrorTER(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(PtMirrorTile mirrorTile, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {

        Vector3d facing = mirrorTile.getMirrorNormalVector();

        Direction baseDirection = mirrorTile.getBlockState().getValue(FACING);

        ModelManager manager = Minecraft.getInstance().getModelManager();

        // init model
        IBakedModel supportModel = manager.getModel(PtConstants.MODELS.MIRROR_SUPPORT);
        IBakedModel frameModel = manager.getModel(PtConstants.MODELS.MIRROR_FRAME);
        IBakedModel mirrorModel = manager.getModel(PtConstants.MODELS.SILVER_MIRROR);

        double xSupportR = 0, ySupportR = 0, zSupportR = 0;
        double xFrameR = 0, yFrameR = 0, zFrameR = 0;

        // init rotation angle
        switch (baseDirection) {
            case DOWN:
                ySupportR = 0.5 * PI - MathHelper.atan2(facing.z, facing.x);
                xFrameR = -Math.atan(facing.y / MathHelper.sqrt(this.sumOfSquare(facing.x, facing.z)));
                break;

            case UP:
                zSupportR = PI;
                ySupportR = - 0.5 * Math.PI - MathHelper.atan2(facing.z, facing.x);
                xFrameR = -Math.atan(facing.y / MathHelper.sqrt(this.sumOfSquare(facing.x, facing.z)));
                yFrameR = PI;
                zFrameR = -PI;
                break;

            case NORTH:
                xSupportR = 0.5 * PI;
                ySupportR = -Math.atan2(facing.x ,facing.y);
                xFrameR = PI + Math.atan2(facing.z, MathHelper.sqrt(this.sumOfSquare(facing.x, facing.y)));
                break;

            case SOUTH:
                xSupportR = -0.5 * PI;
                ySupportR = Math.atan2(facing.x ,facing.y);
                xFrameR = Math.atan2(facing.z, MathHelper.sqrt(this.sumOfSquare(facing.x, facing.y)));
                break;

            case EAST:
                zSupportR = 0.5 * PI;
                xSupportR = -Math.atan2(facing.y, facing.z);
                xFrameR = Math.atan2(facing.x, MathHelper.sqrt(this.sumOfSquare(facing.y, facing.z)));
                break;

            case WEST:
                zSupportR = -0.5 * PI;
                xSupportR = -Math.atan2(facing.y, facing.z);
                xFrameR = -Math.atan2(facing.x, MathHelper.sqrt(this.sumOfSquare(facing.y, facing.z)));
                break;
        }

        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        PtModelRenderer modelRenderer = PtModelRenderer.create(blockRenderer, iRenderTypeBuffer, mirrorTile.getBlockState(), combinedLightIn, combinedOverlayIn);

        // begin render
        matrixStack.pushPose();

        // render support
        matrixStack.translate(.5, .5, .5);
        matrixStack.mulPose(Vector3f.XP.rotation((float) xSupportR));
        matrixStack.mulPose(Vector3f.YP.rotation((float) ySupportR));
        matrixStack.mulPose(Vector3f.ZP.rotation((float) zSupportR));
        matrixStack.translate(-.5, -.5, -.5);

        modelRenderer.renderModel(matrixStack, supportModel, RenderType.solid());


        // render frame
        matrixStack.translate(.5, .53125, .5);
        matrixStack.mulPose(Vector3f.XP.rotation((float) xFrameR));
        matrixStack.mulPose(Vector3f.YP.rotation((float) yFrameR));
        matrixStack.mulPose(Vector3f.ZP.rotation((float) zFrameR));
        matrixStack.translate(-.5, -.53125, -.5);

        modelRenderer.renderModel(matrixStack, frameModel, RenderType.solid());
        modelRenderer.renderModel(matrixStack, mirrorModel, RenderType.solid());

        // end render
        matrixStack.popPose();
    }

    private double sumOfSquare(double a, double b) {
        return a*a + b*b;
    }

}
