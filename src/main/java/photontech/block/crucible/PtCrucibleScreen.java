package photontech.block.crucible;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.LogManager;
import photontech.utils.screen.IScreenFluidRenderer;
import photontech.utils.screen.PtBaseScreen;
import photontech.utils.Utils;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PtCrucibleScreen extends PtBaseScreen<PtCrucibleContainer> implements IScreenFluidRenderer {

    private final ResourceLocation CRUCIBLE_CONTAINER = new ResourceLocation(Utils.MOD_ID, "textures/gui/crucible.png");
    // FluidTank绘制的起始坐标（左下角）
    private final int fluidTankBeginX = 60;
    private final int fluidTankBeginY = 101;
    // FLuidTank的长宽
    private final int tankWidth = 82;
    private final int tankHeight = 82;
    // 绘制机器物品栏的起始坐标（左上角）
//    private final int inventoryBeginX = 71;
//    private final int inventoryBeginY = 33;

    public PtCrucibleScreen(PtCrucibleContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 173;
        this.imageHeight = 214;
        this.inventoryLabelY = this.imageHeight - 92;
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        int beginX = (this.width - this.imageWidth) / 2;
        int beginY = (this.height - this.imageHeight) / 2;

        // Temperature
        if (isMouseInArea(beginX + 10, beginY + 18, 6, 84, mouseX, mouseY)) {
            NonNullList<ITextComponent> temperatureInfo = NonNullList.create();
            temperatureInfo.add(new TranslationTextComponent("text." + Utils.MOD_ID + ".current_temperature", String.format("%.2f", this.menu.getTemperature())));
            temperatureInfo.add(new TranslationTextComponent("text." + Utils.MOD_ID + ".overload_temperature", String.format("%.2f", this.menu.getOverloadTemperature())));
            renderComponentTooltip(matrixStack, temperatureInfo, mouseX, mouseY);
        }

//        // Melting process
//        if (isMouseInArea(beginX + 101, beginY + 84, 24, 17, mouseX, mouseY)) {
//            NonNullList<ITextComponent> processInfo = NonNullList.create();
//            processInfo.add(new TranslationTextComponent("text." + Utils.MOD_ID + ".recipe_process", String.format("%.1f", this.menu.getCoolingProcess() * 100)));
//            renderComponentTooltip(matrixStack, processInfo, mouseX, mouseY);
//        }
//
//        // Cooling process
//        if (isMouseInArea(beginX + 101, beginY + 38, 24, 17, mouseX, mouseY)) {
//            NonNullList<ITextComponent> processInfo = NonNullList.create();
//            processInfo.add(new TranslationTextComponent("text." + Utils.MOD_ID + ".recipe_process", String.format("%.1f", this.menu.getMeltingProcess() * 100)));
//            renderComponentTooltip(matrixStack, processInfo, mouseX, mouseY);
//        }
//
//        // Other process
//        if (isMouseInArea(beginX + 53, beginY + 56, 17, 24, mouseX, mouseY)) {
//            NonNullList<ITextComponent> processInfo = NonNullList.create();
//            processInfo.add(new TranslationTextComponent("text." + Utils.MOD_ID + ".recipe_process", String.format("%.1f", this.menu.getOtherProcess() * 100)));
//            renderComponentTooltip(matrixStack, processInfo, mouseX, mouseY);
//        }

        drawMultiFluidTank(matrixStack, mouseX, mouseY, beginX + fluidTankBeginX, beginY + fluidTankBeginY);
        if (isMouseInArea(beginX + 60, beginY + 19, 82, 82, mouseX, mouseY)) {
            if (!hasControlDown())
            renderTooltip(matrixStack, mouseX, mouseY);
        }
        else renderTooltip(matrixStack, mouseX, mouseY);

    }

    /**
     * @param beginX,beginY determines left-down corner
     */
    private void drawMultiFluidTank(MatrixStack matrixStack, int mouseX, int mouseY, int beginX, int beginY) {
        int tanks = this.menu.getFluidTanks().getTanks();
        for (int i = 0, yOffset = 0; i < tanks; ++i) {
            FluidStack stack = this.menu.getFluidTanks().getFluidInTank(i);

            if (stack.isEmpty()) {
                continue;
            }

            int leftDownY = beginY - yOffset;

            // 使用接口提供的方法进行流体渲染
            int fluidHeight = drawFluid(matrixStack, beginX, leftDownY, stack);

            // 渲染鼠标指针经过时的指示标签
            if (hasControlDown() && this.isMouseInArea(beginX, leftDownY - fluidHeight, tankWidth, fluidHeight, mouseX, mouseY)) {
                NonNullList<ITextComponent> stackInfo = NonNullList.create();
                stackInfo.add(new TranslationTextComponent(stack.getTranslationKey()));
                stackInfo.add(new StringTextComponent(stack.getAmount() + "mB"));
                renderComponentTooltip(
                        matrixStack,
                        stackInfo,
                        mouseX,
                        mouseY
                );
            }
            // 更新流体高度
            yOffset += fluidHeight;
        }
    }


    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {

        this.renderBackground(matrixStack);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bind(CRUCIBLE_CONTAINER);
        int beginX = (this.width - this.imageWidth) / 2;
        int beginY = (this.height - this.imageHeight) / 2;
        blit(matrixStack, beginX, beginY, 0, 0, imageWidth, imageHeight, this.textureWidth, this.textureHeight);

        // draw temperature indicator
        int tempHeight = (int) (this.menu.getTemperature() / this.menu.getOverloadTemperature() * 82);
        tempHeight = Math.min(tempHeight, 82);
        tempHeight = Math.max(tempHeight, 0);
        blit(matrixStack, beginX + 11, beginY + 101 - tempHeight, 173, 154 - tempHeight, 4, tempHeight);

//        blit(matrixStack, beginX + 77, beginY + 34, 197, 0, 54, 54);


//        // draw melting process
//        int processWidth = (int) (this.menu.getMeltingProcess() * 24);
//        processWidth = Math.min(processWidth, 24);
//        processWidth = Math.max(processWidth, 0);
//        blit(matrixStack, beginX + 101, beginY + 38, 173, 0, processWidth, 17);
//
//        // draw cooling process
//        processWidth = (int) (this.menu.getCoolingProcess() * 24);
//        processWidth = Math.min(processWidth, 24);
//        processWidth = Math.max(processWidth, 0);
//        blit(matrixStack, beginX + 125 - processWidth, beginY + 84, 197 - processWidth, 17, processWidth, 17);
//
//        // draw other process
//        int processHeight = (int) (this.menu.getOtherProcess() * 24);
//        processHeight = Math.min(processHeight, 24);
//        processHeight = Math.max(processHeight, 0);
//        blit(matrixStack, beginX + 53, beginY + 56, 173, 34, 17, processHeight);


    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public int getTankHeight() {
        return this.tankHeight;
    }

    @Override
    public int getTankWidth() {
        return this.tankWidth;
    }


    //    @Override
//    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
//    }
}
