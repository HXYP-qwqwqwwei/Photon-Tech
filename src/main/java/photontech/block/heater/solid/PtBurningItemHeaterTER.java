package photontech.block.heater.solid;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.EmptyModelData;

public class PtBurningItemHeaterTER extends TileEntityRenderer<PtBurningItemHeaterTile> {


    public PtBurningItemHeaterTER(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(PtBurningItemHeaterTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        if (tileEntityIn.getBlockState().getValue(BlockStateProperties.LIT)) {

            matrixStackIn.translate(0.125, 0.125, 0.125);
            matrixStackIn.scale(0.75F, 0.75F, 0.75F);
            BlockState state = Blocks.FIRE.defaultBlockState();
            blockRenderer.renderBlock(state, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, EmptyModelData.INSTANCE);

        }

        matrixStackIn.popPose();
    }
}
