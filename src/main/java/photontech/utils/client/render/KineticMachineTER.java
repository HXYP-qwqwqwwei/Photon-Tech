package photontech.utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import photontech.block.kinetic.KineticMachine;

import javax.annotation.Nonnull;

public class KineticMachineTER<T extends KineticMachine> extends MachineTileRenderer<T> {

    public KineticMachineTER(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(@Nonnull T machine, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        for (RenderType type : RenderType.chunkBufferLayers()) {
            if (RenderTypeLookup.canRenderInLayer(machine.getBlockState(), type)){
                renderRotatingBuffer(machine, bufferFromTileEntity(machine), matrixStack, bufferIn.getBuffer(type), combinedLightIn);
                if (machine.getAxleBlockState() != null) {
                    renderRotatingBuffer(machine, bufferFromBlockState(machine.getAxleBlockState()), matrixStack, bufferIn.getBuffer(type), combinedLightIn);
                }
            }
        }
    }


    /**
     * 渲染旋转后的模型
     * @param te 当前te状态
     * @param superBuffer 模型原始buffer
     */
    public static void renderRotatingBuffer(KineticMachine te, SuperByteBuffer superBuffer, MatrixStack ms, IVertexBuilder buffer, int light) {
        rotateBuffer(superBuffer, te.getAxis(), te.getAngle(), light);
        superBuffer.renderInto(ms, buffer);
    }


}
