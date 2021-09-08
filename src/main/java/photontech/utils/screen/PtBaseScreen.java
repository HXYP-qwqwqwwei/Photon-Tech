package photontech.utils.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

public abstract class PtBaseScreen<T extends Container> extends ContainerScreen<T> {

    protected final int textureWidth = 256;
    protected final int textureHeight = 256;

    public PtBaseScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }


    protected boolean isMouseInArea(int xMin, int yMin, int dx, int dy, int mouseX, int mouseY) {
        return (mouseX >= xMin && mouseY >= yMin && mouseX < xMin + dx && mouseY < yMin + dy);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }
}
