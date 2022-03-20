package net.mehvahdjukaar.tron_digitized.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.entity.ScreenEntity;
import net.mehvahdjukaar.tron_digitized.init.ClientSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public class ScreenEntityRenderer extends EntityRenderer<ScreenEntity> {
    private final BlockRenderDispatcher blockRenderer;
    private final List<ScreenWidget> entries = new ArrayList<>();

    public ScreenEntityRenderer(EntityRendererProvider.Context p_174332_) {
        super(p_174332_);
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        //add here more widgets. params are: start pos (x,y), end pos (x,y), start scale, end scale

        this.entries.add(new ScreenWidget(ClientSetup.SCREEN, 0, 0, 1, 1, 1, 0.5f));
        this.entries.add(new ScreenWidget(ClientSetup.SCREEN2, 2, 4, 6, 3, 1, 0.5f));
        //this.entries.add(new ScreenWidget(ClientSetup.SCREEN2, 0, 0, 1, 1, 1, 0.5f));
    }


    @Override
    public void render(ScreenEntity entity, float pEntityYaw, float pPartialTicks, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight) {
        poseStack.pushPose();

        this.entries.clear();
        this.entries.add(new ScreenWidget(ClientSetup.SCREEN, 2, 2, 6, 6, 1, 0.5f));
        this.entries.add(new ScreenWidget(ClientSetup.SCREEN2, 2, 4, 6, 3, 1, 0.5f));

        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - pEntityYaw));

        float a = entity.getAnimation(pPartialTicks);
        entries.forEach(e -> e.render(poseStack, pBuffer, blockRenderer, pPackedLight, a));

        poseStack.popPose();
        super.render(entity, pEntityYaw, pPartialTicks, poseStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(ScreenEntity entity) {
        return null;
    }


    public static class ScreenWidget {
        private final Vec2 start;
        private final Vec2 end;
        private final ResourceLocation model;
        private final float scale0;
        private final float scale1;

        public ScreenWidget(ResourceLocation model, float x0, float y0, float x1, float y1, float scale0, float scale1) {
            this.start = new Vec2(x0, y0);
            this.end = new Vec2(x1, y1);
            ;
            this.model = model;
            this.scale0 = scale0;
            this.scale1 = scale1;
        }

        public void render(PoseStack poseStack, MultiBufferSource pBuffer, BlockRenderDispatcher renderer,
                           int pPackedLight, float animation) {
            poseStack.pushPose();
            float x = Mth.lerp(animation, start.x, end.x);
            float y = Mth.lerp(animation, start.y, end.y);
            float s = Mth.lerp(animation, scale0, scale1);

            poseStack.scale(s, s, 1);
            poseStack.translate(-0.5, -0.5, -0.5);
            poseStack.translate(x, y, 0);
            TronBlockTileRenderer.renderBlockModel(model, poseStack, pBuffer, renderer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, true);
            poseStack.popPose();
        }
    }
}