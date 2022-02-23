package net.mehvahdjukaar.tron_digitized.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.common.entity.IHealableEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;

public class DigitizedGuiOverlay extends Gui implements IIngameOverlay {
    private static final DigitizedGuiOverlay INSTANCE = new DigitizedGuiOverlay(Minecraft.getInstance());

    public static final ResourceLocation texture = Tron.res("textures/entity/healing_overlay.png");

    public DigitizedGuiOverlay(Minecraft pMinecraft) {
        super(pMinecraft);
    }

    public static void register() {
        OverlayRegistry.registerOverlayBelow(ForgeIngameGui.FROSTBITE_ELEMENT, "Digitized", INSTANCE);
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack mStack, float partialTicks, int width, int height) {
        if(this.minecraft.player instanceof IHealableEntity iHealable && iHealable.isInHealingChamber()) {
            float alpha = iHealable.getHealingFade(partialTicks);
            gui.setupOverlayRenderState(true, false);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            RenderSystem.setShaderTexture(0, texture);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.vertex(0.0D, height, -90.0D).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width, height, -90.0D).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(width, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }



}
