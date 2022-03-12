package photontech.utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import photontech.block.kinetic.axle.KtMachineTile;
import photontech.event.ClientEventHandler;

import javax.annotation.Nonnull;

import static photontech.utils.client.render.Compartment.KINETIC_TILE;
import static photontech.utils.client.render.Compartment.GENERIC_MODEL;

public class KtMachineTER<T extends KtMachineTile> extends TileEntityRenderer<T> {
    public KtMachineTER(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(@Nonnull T axle, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        for (RenderType type : RenderType.chunkBufferLayers()) {
            if (RenderTypeLookup.canRenderInLayer(axle.getBlockState(), type)){
                renderRotatingBuffer(axle, getRotatedModel(axle), matrixStack, bufferIn.getBuffer(type), combinedLightIn);
                if (axle.getAxleBlockState() != null) {
                    renderRotatingBuffer(axle, getRotatedModel(axle.getAxleBlockState()), matrixStack, bufferIn.getBuffer(type), combinedLightIn);
                }
            }
        }
    }

    protected SuperByteBuffer getRotatedModel(T te) {
        return ClientEventHandler.BUFFER_CACHE.renderBlockIn(KINETIC_TILE, te.getBlockState());
    }

    protected SuperByteBuffer getRotatedModel(BlockState blockState) {
        return ClientEventHandler.BUFFER_CACHE.renderBlockIn(KINETIC_TILE, blockState);
    }

    protected SuperByteBuffer createBufferFromModel(BlockState blockState, ResourceLocation id) {
        return ClientEventHandler.BUFFER_CACHE.renderModelIn(GENERIC_MODEL, id, blockState);
    }

    public static void renderStaticBuffer(SuperByteBuffer superByteBuffer, MatrixStack ms, IVertexBuilder buffer, int light) {
        superByteBuffer.light(light).renderInto(ms, buffer);
    }

    public static void renderRotatingBuffer(KtMachineTile te, SuperByteBuffer superBuffer, MatrixStack ms, IVertexBuilder buffer, int light) {
        standardKineticRotationTransform(superBuffer, te, light).renderInto(ms, buffer);
    }

    public static SuperByteBuffer standardKineticRotationTransform(SuperByteBuffer buffer, KtMachineTile te, int light) {
        return kineticRotationTransform(buffer, te.getAxis(), te.getAngle(), light);
    }

    public static SuperByteBuffer kineticRotationTransform(SuperByteBuffer buffer, Direction.Axis axis, float angle, int light) {
        buffer.light(light);
        buffer.rotateCentered(Direction.get(Direction.AxisDirection.POSITIVE, axis), angle);
        return buffer;
    }
}
