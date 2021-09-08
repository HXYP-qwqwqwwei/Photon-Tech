package photontech.block.crucible;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.util.Direction;
import photontech.utils.helper.CrucibleConnections;
import photontech.utils.capability.fluid.PtMultiFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

import static net.minecraft.util.Direction.*;

public class PtCrucibleTER extends TileEntityRenderer<PtCrucibleTileEntity> {
    private static final double posX = 0.125;
    private static final double posY = 0.125;
    private static final double posZ = 0.125;
    private static final float scaleX = 0.75F;
    private static final float scaleY = 0.8125F;
    private static final float scaleZ = 0.75F;
    private static final float offset = 0.125F;

    private static final int[] directX = {
            1, 1, 0, 0, // TOP
            1, 1, 0, 0, // BOTTOM
            1, 1, 1, 1, // EAST
            0, 0, 0, 0, // WEST
            1, 1, 0, 0, // SOUTH
            0, 0, 1, 1  // NORTH
    };

    private static final int[] directY = {
            1, 1, 1, 1, // TOP
            0, 0, 0, 0, // BOTTOM
            0, 1, 1, 0, // EAST
            0, 1, 1, 0, // WEST
            0, 1, 1, 0, // SOUTH
            0, 1, 1, 0  // NORTH
    };

    private static final int[] directZ = {
            1, 0, 0, 1, // TOP
            0, 1, 1, 0, // BOTTOM
            0, 0, 1, 1, // EAST
            1, 1, 0, 0, // WEST
            1, 1, 1, 1, // SOUTH
            0, 0, 0, 0  // NORTH
    };

    public static final Direction[] directions = {EAST, WEST, SOUTH, NORTH};

    public PtCrucibleTER(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    // add renderer vertex
    private void add(PtCrucibleTileEntity tileEntityIn, IVertexBuilder renderer, MatrixStack matrixStackIn, int color, float x, float y, float z, float u, float v) {
        renderer.vertex(matrixStackIn.last().pose(), x, y, z)
                .color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF)
                .uv(u, v)
                .uv2(WorldRenderer.getLightColor(Objects.requireNonNull(tileEntityIn.getLevel()), tileEntityIn.getBlockPos()))
                .normal(1.0F, 0, 0)
                .endVertex();
    }

    @Override
    public void render(PtCrucibleTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();

        BlockState state = tileEntityIn.getBlockState();
        CrucibleConnections crucible = CrucibleConnections.getInstance(state);
        if (crucible == null) {
            matrixStackIn.popPose();
            return;
        }

        // get connect state
        int eastConnected = crucible.connectedTo(EAST) ? 1 : 0;
        int westConnected = crucible.connectedTo(WEST) ? 1 : 0;
        int southConnected = crucible.connectedTo(SOUTH) ? 1 : 0;
        int northConnected = crucible.connectedTo(NORTH) ? 1 : 0;
        int maxFilling = tileEntityIn.getMaxFilling();

        PtMultiFluidTank tanks = tileEntityIn.getFluidTanks();
        int capacity = tanks.getCapacity();
        int size = tanks.getTanks();

        int nowFilling = 0;

        for (int i = 0; i < size; ++i) {
            FluidStack tank = tanks.getFluidInTank(i);
            if (tank.isEmpty()) {
                continue;
            }

            // get tank filling
            int amount = tank.getAmount();
            int filling = (int) Math.ceil((amount - 0.1) / capacity * maxFilling);


            // calculate position and scale
            float sx = scaleX + (eastConnected + westConnected) * offset;
            float sy = scaleY * filling / maxFilling;
            float sz = scaleZ + (southConnected + northConnected) * offset;

            double px = posX - westConnected * offset;
            double py = posY + scaleY * nowFilling / maxFilling;
            double pz = posZ - northConnected * offset;

            // init renderer matrix
            matrixStackIn.translate(px, py, pz);
            matrixStackIn.scale(sx, sy, sz);

            // init texture
            Fluid fluid = tank.getFluid();
            int color = fluid.getAttributes().getColor();
            TextureAtlasSprite spriteStill = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(fluid.getAttributes().getStillTexture());

            IVertexBuilder builder = bufferIn.getBuffer(RenderType.translucent());

            // render top and bottom face
            for (int k = 0; k < 8;) {
                add(tileEntityIn, builder, matrixStackIn, color, directX[k], directY[k], directZ[k++], spriteStill.getU1(), spriteStill.getV1());
                add(tileEntityIn, builder, matrixStackIn, color, directX[k], directY[k], directZ[k++], spriteStill.getU1(), spriteStill.getV0());
                add(tileEntityIn, builder, matrixStackIn, color, directX[k], directY[k], directZ[k++], spriteStill.getU0(), spriteStill.getV0());
                add(tileEntityIn, builder, matrixStackIn, color, directX[k], directY[k], directZ[k++], spriteStill.getU0(), spriteStill.getV1());
            }

            // render EWSN faces
            float maxV = spriteStill.getV1();
            float minV = spriteStill.getV0();
            float trueMaxV = minV + (maxV - minV) * filling / maxFilling;
            for (int k = 8; k < 24;) {
                if (crucible.connectedTo(directions[(k - 8) / 4])) {
                    k += 4;
                    continue;
                }
                add(tileEntityIn, builder, matrixStackIn, color, directX[k], directY[k], directZ[k++], spriteStill.getU1(), trueMaxV);
                add(tileEntityIn, builder, matrixStackIn, color, directX[k], directY[k], directZ[k++], spriteStill.getU1(), minV);
                add(tileEntityIn, builder, matrixStackIn, color, directX[k], directY[k], directZ[k++], spriteStill.getU0(), minV);
                add(tileEntityIn, builder, matrixStackIn, color, directX[k], directY[k], directZ[k++], spriteStill.getU0(), trueMaxV);
            }

            // update nowfilling and backwards renderer matrix
            nowFilling += filling;
            matrixStackIn.scale(1/sx, 1/sy, 1/sz);
            matrixStackIn.translate(-px, -py, -pz);
        }

        matrixStackIn.popPose();
    }
}
