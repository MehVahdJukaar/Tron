package net.mehvahdjukaar.tron_digitized.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;

public class DiscEditingGui extends Screen {

    public static void playSound(Player pPlayer, double pX, double pY, double pZ, SoundEvent pSound, SoundSource pCategory, float pVolume, float pPitch) {
        pPlayer.level.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModRegistry.DIGITAL_SOUND.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private static final ResourceLocation DISC_GUI[] = new ResourceLocation[]{
            Tron.res("textures/blocks/discs/basic/basic.png")};
    private int discGui;

    protected DiscEditingGui(InputEvent.KeyInputEvent event) {
        super(new TranslatableComponent("gui.tron.disc_editing"));
    }

    public static void open(InputEvent.KeyInputEvent event) {
        Minecraft.getInstance().setScreen(new DiscEditingGui(event));
        Minecraft.getInstance().player.playSound(ModRegistry.DIGITAL_SOUND.get(), 1.0F, 1.0F);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, DISC_GUI[discGui]);
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

}