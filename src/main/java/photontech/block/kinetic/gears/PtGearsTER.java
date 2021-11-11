package photontech.block.kinetic.gears;

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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import photontech.utils.PtConstants;
import photontech.utils.Utils;

import javax.annotation.Nonnull;

public class PtGearsTER extends TileEntityRenderer<PtGearsTile> {

    public PtGearsTER(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(PtGearsTile gearsTile, float partialTicks, MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        IBakedModel woodenGearModel = manager.getModel(PtConstants.MODELS.WOODEN_GEAR_MODEL);

        matrixStack.pushPose();

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(Vector3f.XP.rotation(gearsTile.getAngle(Direction.EAST)));
        matrixStack.translate(-0.5, -0.5, -0.5);

        blockRenderer.getModelRenderer().renderModel(
                matrixStack.last(),
                bufferIn.getBuffer(RenderType.solid()),
                gearsTile.getBlockState(),
                woodenGearModel,
                1F, 1F, 1F,
                combinedLightIn, combinedOverlayIn,
                EmptyModelData.INSTANCE
        );

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(Vector3f.XP.rotation(-gearsTile.getAngle(Direction.EAST)));
        matrixStack.translate(-0.5, -0.5, -0.5);

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(Vector3f.YP.rotation((float) (Math.PI * 0.5)));
//        matrixStack.mulPose(Vector3f.XP.rotation((float) (Math.PI * 0.125)));
        matrixStack.mulPose(Vector3f.XP.rotation(gearsTile.getAngle(Direction.NORTH)));
        matrixStack.translate(-0.5, -0.5, -0.5);
        blockRenderer.getModelRenderer().renderModel(
                matrixStack.last(),
                bufferIn.getBuffer(RenderType.solid()),
                gearsTile.getBlockState(),
                woodenGearModel,
                1F, 1F, 1F,
                combinedLightIn, combinedOverlayIn,
                EmptyModelData.INSTANCE
        );

        matrixStack.popPose();
    }
}
