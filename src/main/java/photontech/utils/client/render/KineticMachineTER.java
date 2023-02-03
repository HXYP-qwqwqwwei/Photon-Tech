package photontech.utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import photontech.block.kinetic.KineticMachine;
import photontech.utils.helper.fuctions.AxisHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KineticMachineTER<T extends KineticMachine> extends MachineTileRenderer<T> {
    public static final float HALF_PI = (float) Math.PI / 2;

    public KineticMachineTER(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(@Nonnull T axle, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        for (RenderType type : RenderType.chunkBufferLayers()) {
            if (RenderTypeLookup.canRenderInLayer(axle.getBlockState(), type)){
                renderRotatingBuffer(axle, getBlockModel(axle), matrixStack, bufferIn.getBuffer(type), combinedLightIn);
                if (axle.getAxleBlockState() != null) {
                    renderRotatingBuffer(axle, getBlockModel(axle.getAxleBlockState()), matrixStack, bufferIn.getBuffer(type), combinedLightIn);
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
        standardKineticRotationTransform(superBuffer, te, light).renderInto(ms, buffer);
    }

    /**
     * 标准动力学旋转变换
     * @param buffer 模型buffer
     * @param te 当前TE状态，包含角度、转轴等信息
     * @param light 当前光照信息
     * @return 变换后的buffer
     */
    public static SuperByteBuffer standardKineticRotationTransform(SuperByteBuffer buffer, KineticMachine te, int light) {
        return kineticRotationTransform(buffer, te.getAxis(), te.getAngle(), light);
    }

    /**
     * 根据转轴进行旋转和光照变换
     * @param buffer 模型buffer
     * @param axis 旋转轴，如果为null则不旋转
     * @param angle 按axis为转轴旋转的角度
     * @param light 当前光照
     * @return 变换后的buffer
     */
    public static SuperByteBuffer kineticRotationTransform(SuperByteBuffer buffer, @Nullable Direction.Axis axis, float angle, int light) {
        buffer.light(light);
        if (axis != null) {
            buffer.rotateCentered(AxisHelper.getAxisPositiveDirection(axis), angle);
        }
        return buffer;
    }


}
