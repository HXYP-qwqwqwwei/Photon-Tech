package photontech.utils.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;

public interface IScreenFluidRenderer {

    int TEX_WIDTH = 16;
    int TEX_HEIGHT = 16;

    int getCapacity();

    int getTankHeight();

    int getTankWidth();

    default int getMinFluidHeight() {
        return 1;
    }

    /**
     *
     * @param matrixStack matrixStack
     * @param xPosition beginX (left)
     * @param yPosition beginY (down)
     * @param fluidStack fluidStack
     * @return rendered height
     */
    default int drawFluid(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable FluidStack fluidStack) {
        if (fluidStack == null || fluidStack.isEmpty()) {
            return 0;
        }

        Fluid fluid = fluidStack.getFluid();


        FluidAttributes attributes = fluid.getAttributes();
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(attributes.getStillTexture());
        int fluidColor = attributes.getColor();

        int amount = fluidStack.getAmount();
        int fluidLevel = (amount * getTankHeight()) / getCapacity();
        fluidLevel = Math.max(fluidLevel, getMinFluidHeight());
        fluidLevel = Math.min(fluidLevel, getTankHeight());

        drawTiledSprite(matrixStack, xPosition, yPosition, fluidColor, fluidLevel, fluidStillSprite);
        return fluidLevel;
    }

    default void drawTiledSprite(MatrixStack matrixStack, int xPosition, int yPosition, int color, int renderHeight, TextureAtlasSprite sprite) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(PlayerContainer.BLOCK_ATLAS);
        Matrix4f matrix = matrixStack.last().pose();
        setGLColorFromInt(color);

        final int xTileCount = getTankWidth() / TEX_WIDTH;
        final int xRemainder = getTankWidth() - (xTileCount * TEX_WIDTH);
        final int yTileCount = renderHeight / TEX_HEIGHT;
        final int yRemainder = renderHeight - (yTileCount * TEX_HEIGHT);


        for (int xTile = 0; xTile <= xTileCount; ++xTile) {
            for (int yTile = 0; yTile <= yTileCount; ++yTile) {

                int width = (xTile == xTileCount) ? xRemainder : TEX_WIDTH;
                int height = (yTile == yTileCount) ? yRemainder : TEX_HEIGHT;

                int x = xPosition + (xTile * TEX_WIDTH);
                int y = yPosition - (yTile * TEX_HEIGHT);

                if (width > 0 && height > 0) {
                    drawTextureWithMasking(matrix, x, y, sprite, width, height, 100);
                }
            }
        }

        RenderSystem.color4f(1, 1, 1, 1);
    }

    default void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.color4f(red, green, blue, alpha);
    }

    default void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int width, int height, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax = uMin + (width / 16F * (uMax - uMin));
        vMin = vMax - (height / 16F * (vMax - vMin));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.vertex(matrix, xCoord, yCoord, zLevel).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + width, yCoord, zLevel).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + width, yCoord - height, zLevel).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(matrix, xCoord, yCoord - height, zLevel).uv(uMin, vMin).endVertex();
        tessellator.end();
    }
}
