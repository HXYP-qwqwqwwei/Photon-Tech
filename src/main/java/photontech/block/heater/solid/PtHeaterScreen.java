package photontech.block.heater.solid;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import photontech.utils.Utils;
import photontech.utils.screen.PtBaseScreen;

public class PtHeaterScreen extends PtBaseScreen<PtHeaterContainer> {

    private final ResourceLocation HEATER_CONTAINER = new ResourceLocation(Utils.MOD_ID, "textures/gui/heater.png");

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        int beginX = (this.width - this.imageWidth) / 2;
        int beginY = (this.height - this.imageHeight) / 2;

        // Burning process
        if (isMouseInArea(beginX + 73, beginY + 33, 24, 17, mouseX, mouseY)) {
            NonNullList<ITextComponent> processInfo = NonNullList.create();
            processInfo.add(new TranslationTextComponent("text.photontech.recipe_process", String.format("%.1f", this.menu.getBurningProcess() * 100)));
            renderComponentTooltip(matrixStack, processInfo, mouseX, mouseY);
        }

    }

    public PtHeaterScreen(PtHeaterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 174;
        this.imageHeight = 164;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.renderBackground(matrixStack);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(HEATER_CONTAINER);
        int beginX = (this.width - this.imageWidth) / 2;
        int beginY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, beginX, beginY, 0, 0, imageWidth, imageHeight, this.textureWidth, this.textureHeight);

        // draw burning process
        int processWidth = (int) (this.menu.getBurningProcess() * 24);
        processWidth = Math.min(processWidth, 24);
        processWidth = Math.max(processWidth, 0);
        blit(matrixStack, beginX + 73, beginY + 33, 174, 14, processWidth, 17);

        // draw is-burning
        if (this.menu.isBurning()) {
            blit(matrixStack, beginX + 50, beginY + 17, 174, 0, 14, 14);
        }

    }
}
