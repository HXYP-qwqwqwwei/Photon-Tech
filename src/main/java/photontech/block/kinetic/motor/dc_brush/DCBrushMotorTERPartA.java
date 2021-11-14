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
import photontech.utils.render.PtModelRenderer;

import javax.annotation.Nonnull;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class DCBrushMotorTERPartA extends TileEntityRenderer<DCBrushTilePartA> {

    public DCBrushMotorTERPartA(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(@Nonnull DCBrushTilePartA partATile, float partialTicks, MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        PtModelRenderer modelRenderer = PtModelRenderer.create(blockRenderer, bufferIn, partATile.getBlockState(), combinedLightIn, combinedOverlayIn);
        ModelManager manager = Minecraft.getInstance().getModelManager();

        matrixStack.pushPose();

        // 初始化朝向
        Direction facing = partATile.getBlockState().getValue(FACING);
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        float angle = partATile.getAngle(facing);

        switch (facing) {
            case EAST:
                matrixStack.mulPose(Vector3f.YP.rotation((float) Math.PI));
                break;
            case UP:
                matrixStack.mulPose(Vector3f.ZP.rotation((float) (Math.PI * 1.5)));
                break;
            case DOWN:
                matrixStack.mulPose(Vector3f.ZP.rotation((float) (Math.PI * 0.5)));
                break;
            case SOUTH:
                matrixStack.mulPose(Vector3f.YP.rotation((float) (Math.PI * 0.5)));
                break;
            case NORTH:
                matrixStack.mulPose(Vector3f.YP.rotation((float) (Math.PI * 1.5)));
                break;
        }
        matrixStack.translate(-0.5F, -0.5F, -0.5F);
        // 渲染固定不动的电刷部分
        modelRenderer.renderModel(matrixStack, manager.getModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_A_BRUSH), RenderType.solid());

        // 以下是动画部分
        matrixStack.translate(0.5F, 0.5F, 0.5F);

        // 需要注意的是有些朝向下，需要对旋转角度进行翻转
        switch (facing) {
            case WEST:
            case DOWN:
            case SOUTH:
                matrixStack.mulPose(Vector3f.XP.rotation(angle));
                break;
            default:
                matrixStack.mulPose(Vector3f.XN.rotation(angle));
        }
        matrixStack.translate(-0.5F, -0.5F, -0.5F);

        // 渲染触点部分
        modelRenderer.renderModel(matrixStack, manager.getModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_A_CONTACTOR), RenderType.solid());
        // 渲染8对连接线（这一部分由于BB的角度限制，必须分开渲染）
        for (int i = 0; i < 8; ++i) {
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            matrixStack.mulPose(Vector3f.XP.rotation((float) (Math.PI * 0.25)));
            matrixStack.translate(-0.5F, -0.5F, -0.5F);
            modelRenderer.renderModel(matrixStack, manager.getModel(PtConstants.MODELS.DC_BRUSH_MODEL_PART_A_WIRES), RenderType.solid());
        }

        matrixStack.popPose();
    }
}
