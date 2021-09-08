package photontech.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import photontech.init.PtItems;
import photontech.utils.render.PtRenderType;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class InWorldRenderer {
    @SubscribeEvent
    public static void renderProtractorIndicator(RenderWorldLastEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }
        World world = player.getCommandSenderWorld();
        String pDim = world.dimension().location().toString();

        if (player.getMainHandItem().getItem() == PtItems.PROTRACTOR.get()) {
            ItemStack protractor = player.getMainHandItem();
            CompoundNBT tag = protractor.getOrCreateTag();
            String dimension1 = tag.getString("RefDimension1");
            String dimension2 = tag.getString("RefDimension2");

            if (!dimension1.isEmpty()) {
                if (pDim.equals(dimension1)) {
                    renderBlockOutline(player, event.getMatrixStack(), BlockPos.of(tag.getLong("RefPos1")), 1, 0, 0, 0.5F);
                }
            }
            if (!dimension2.isEmpty()) {
                if (pDim.equals(dimension2)) {
                    renderBlockOutline(player, event.getMatrixStack(), BlockPos.of(tag.getLong("RefPos2")), 0, 1, 0, 0.5F);
                }
            }
        }
    }

    private static void addLine(IVertexBuilder builder, Matrix4f matrix, float x, float y, float z, float dx, float dy, float dz, float r, float g, float b, float a) {
        builder.vertex(matrix, x, y, z)
                .color(r, g, b, a)
                .endVertex();
        builder.vertex(matrix, x + dx, y + dy, z + dz)
                .color(r, g, b, a)
                .endVertex();
    }

    private static void renderBlockOutline(ClientPlayerEntity player, MatrixStack matrixStack, BlockPos pos, float r, float g, float b, float a) {
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        IVertexBuilder builder = buffer.getBuffer(PtRenderType.OVERLAY_LINES);

        matrixStack.pushPose();

        Vector3d cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        matrixStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        Matrix4f positionMatrix = matrixStack.last().pose();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        addLine(builder, positionMatrix, x, y, z, 1, 0, 0, r, g, b, a);
        addLine(builder, positionMatrix, x, y, z, 0, 1, 0, r, g, b, a);
        addLine(builder, positionMatrix, x, y, z, 0, 0, 1, r, g, b, a);

        addLine(builder, positionMatrix, x, y+1, z+1, 1, 0, 0, r, g, b, a);
        addLine(builder, positionMatrix, x, y+1, z+1, 0, -1, 0, r, g, b, a);
        addLine(builder, positionMatrix, x, y+1, z+1, 0, 0, -1, r, g, b, a);

        addLine(builder, positionMatrix, x+1, y, z+1, -1, 0, 0, r, g, b, a);
        addLine(builder, positionMatrix, x+1, y, z+1, 0, 1, 0, r, g, b, a);
        addLine(builder, positionMatrix, x+1, y, z+1, 0, 0, -1, r, g, b, a);

        addLine(builder, positionMatrix, x+1, y+1, z, -1, 0, 0, r, g, b, a);
        addLine(builder, positionMatrix, x+1, y+1, z, 0, -1, 0, r, g, b, a);
        addLine(builder, positionMatrix, x+1, y+1, z, 0, 0, 1, r, g, b, a);

        matrixStack.popPose();

        RenderSystem.disableDepthTest();
        buffer.endBatch();
    }

}
