package photontech.block.axle;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import photontech.utils.Utils;

import javax.annotation.Nonnull;

public class AxleTER extends TileEntityRenderer<AxleTile> {

    public static final ResourceLocation IRON_AXLE_MODEL = new ResourceLocation(Utils.MOD_ID, "special/iron_axle_model");

    public AxleTER(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(@Nonnull AxleTile axleTile, float partialTicks, MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

        matrixStack.pushPose();

        matrixStack.translate(0.5F, 0.5F, 0.5F);
        matrixStack.mulPose(Vector3f.XP.rotation(axleTile.getAngle()));
        matrixStack.translate(-0.5F, -0.5F, -0.5F);


        ModelManager manager = Minecraft.getInstance().getModelManager();
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        // init model
        IBakedModel supportModel = manager.getModel(IRON_AXLE_MODEL);


        blockRenderer.getModelRenderer().renderModel(
                matrixStack.last(),
                bufferIn.getBuffer(RenderType.solid()),
                axleTile.getBlockState(),
                supportModel,
                1F, 1F, 1F,
                combinedLightIn, combinedOverlayIn,
                EmptyModelData.INSTANCE
        );

        matrixStack.popPose();
    }
}
