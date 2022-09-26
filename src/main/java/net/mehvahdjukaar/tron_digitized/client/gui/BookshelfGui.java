package net.mehvahdjukaar.tron_digitized.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BookshelfGui extends Screen {

    private static final ResourceLocation PAGES[] = new ResourceLocation[]{
            Tron.res("textures/blocks/page_1.png"),
            Tron.res("textures/blocks/page_2.png"),
            Tron.res("textures/blocks/page_3.png"),
            Tron.res("textures/blocks/page_4.png"),
            Tron.res("textures/blocks/page_5.png")};
    private int page;

    protected BookshelfGui(BlockPos pos) {
        super(Component.translatable("gui.tron.bookshelf"));
        this.page = Math.abs(pos.hashCode()) % 5;
    }

    public static void open(BlockPos pos) {
        Minecraft.getInstance().setScreen(new BookshelfGui(pos));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PAGES[page]);
        matrixStack.pushPose();
        matrixStack.translate(this.width / 2f, this.height / 2f, 0);
        float scale = 1;
        matrixStack.scale(scale, scale, 0);
        int imageWidth = 256;
        int imageHeight = 256;
        int k = (-imageWidth) / 2;
        int l = (-imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, imageWidth, imageHeight);
        matrixStack.popPose();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            this.page = (++this.page) % 5;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModRegistry.BOOKSHELF_SOUND.get(), 1.0F));
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}