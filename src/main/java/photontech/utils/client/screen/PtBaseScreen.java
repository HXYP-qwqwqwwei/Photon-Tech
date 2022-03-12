package photontech.utils.client.screen;

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

    /**
     * 判断鼠标指针是否在矩形区域内
     * @param xMin 左上角x
     * @param yMin 左上角y
     * @param dx 长度
     * @param dy 宽度
     * @param mouseX 指针x坐标
     * @param mouseY 指针y坐标
     * @return 如果在区域内，返回true
     */
    protected boolean isMouseInArea(int xMin, int yMin, int dx, int dy, int mouseX, int mouseY) {
        return (mouseX >= xMin && mouseY >= yMin && mouseX < xMin + dx && mouseY < yMin + dy);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
