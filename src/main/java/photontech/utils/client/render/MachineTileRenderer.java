package photontech.utils.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import photontech.event.handler.ClientEventHandler;
import photontech.utils.tileentity.MachineTile;

import javax.annotation.Nonnull;

import static photontech.utils.client.render.Compartment.*;

public abstract class MachineTileRenderer<T extends MachineTile> extends TileEntityRenderer<T> {
    public MachineTileRenderer(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    // 从TE中获得buffer
    protected SuperByteBuffer getBlockModel(T te) {
        return getBlockModel(te.getBlockState());
    }

    // 从BlockState中获取buffer
    protected SuperByteBuffer getBlockModel(BlockState blockState) {
        return ClientEventHandler.BUFFER_CACHE.renderBlockIn(BLOCK_MODEL, blockState);
    }

    // 从ItemStack中获取buffer
    protected SuperByteBuffer getItemModel(BlockState blockState, ItemStack itemStack) {
        return ClientEventHandler.BUFFER_CACHE.renderModelIn(ITEM_MODEL, itemStack, blockState);
    }

    // 从RL中获取buffer
    protected SuperByteBuffer getModel(BlockState blockState, ResourceLocation id) {
        return ClientEventHandler.BUFFER_CACHE.renderModelIn(GENERIC_MODEL, id, blockState);
    }

    protected void renderItemIn(@Nonnull T te, ItemStack itemStack, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer bufferIn) {
        matrixStack.scale(0.25f, 0.25f, 0.25f);
        getItemModel(te.getBlockState(), itemStack).renderInto(matrixStack, bufferIn.getBuffer(RenderType.translucent()));
        matrixStack.scale(4f, 4f, 4f);
    }


}
