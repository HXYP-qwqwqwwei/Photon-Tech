package photontech.utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import photontech.event.handler.ClientEventHandler;
import photontech.utils.helper.fuctions.AxisHelper;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.util.Direction.Axis.X;
import static net.minecraft.util.Direction.Axis.Y;
import static photontech.utils.client.render.Compartment.*;

public abstract class MachineTileRenderer<T extends MachineTile> extends TileEntityRenderer<T> {
    public static final float HALF_PI = (float) Math.PI / 2;
    public static final float PI = (float) Math.PI;


    public MachineTileRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    public abstract void render(@Nonnull T machine, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn);

    /**
     * 根据转轴进行旋转和光照变换
     * @param buffer 模型buffer
     * @param rotatingAxis 旋转轴，如果为null则不旋转
     * @param angle 按axis为转轴旋转的角度
     * @param light 当前光照
     */
    public static void rotateBuffer(SuperByteBuffer buffer, @Nullable Direction.Axis rotatingAxis, float angle, int light) {
        buffer.light(light);
        if (rotatingAxis != null) {
            buffer.rotateCentered(AxisHelper.getAxisPositiveDirection(rotatingAxis), angle);
        }
    }

    public static void rotateBuffer(SuperByteBuffer buffer, @Nullable Direction.Axis rotatingAxis, float angle) {
        if (rotatingAxis != null) {
            buffer.rotateCentered(AxisHelper.getAxisPositiveDirection(rotatingAxis), angle);
        }
    }

    public static void transformByFacing(SuperByteBuffer buffer, Direction facing, int light) {
        switch (facing) {
            case EAST:
                rotateBuffer(buffer, Y, HALF_PI);
                rotateBuffer(buffer, X, HALF_PI);
                break;
            case WEST:
                rotateBuffer(buffer, Y, HALF_PI);
                rotateBuffer(buffer, X, -HALF_PI);
                break;
            case SOUTH:
                rotateBuffer(buffer, X, HALF_PI);
                break;
            case NORTH:
                rotateBuffer(buffer, X, -HALF_PI);
                break;
            case DOWN:
                rotateBuffer(buffer, X, HALF_PI * 2);
                break;
            default: break;
        }
        buffer.light(light);
    }



    // 从TE中获得buffer
    protected static SuperByteBuffer bufferFromTileEntity(TileEntity te) {
        return bufferFromBlockState(te.getBlockState());
    }

    // 从BlockState中获取buffer
    protected static SuperByteBuffer bufferFromBlockState(BlockState blockState) {
        return ClientEventHandler.BUFFER_CACHE.renderBlockIn(BLOCK_MODEL, blockState);
    }

    // 从ItemStack中获取buffer
    protected static SuperByteBuffer bufferFromItemStack(BlockState blockState, ItemStack itemStack) {
        return ClientEventHandler.BUFFER_CACHE.renderModelIn(ITEM_MODEL, itemStack, blockState);
    }

    // 从RL中获取buffer
    protected static SuperByteBuffer bufferFromResourceLocation(BlockState blockState, ResourceLocation id) {
        return ClientEventHandler.BUFFER_CACHE.renderModelIn(GENERIC_MODEL, id, blockState);
    }

    protected void renderItemIn(@Nonnull T te, ItemStack itemStack, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn) {
        matrixStack.scale(0.25f, 0.25f, 0.25f);
        bufferFromItemStack(te.getBlockState(), itemStack).renderInto(matrixStack, bufferIn.getBuffer(RenderType.translucent()));
        matrixStack.scale(4f, 4f, 4f);
    }


}
