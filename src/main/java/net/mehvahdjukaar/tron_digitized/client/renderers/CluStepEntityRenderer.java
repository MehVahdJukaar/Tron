package net.mehvahdjukaar.tron_digitized.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.entity.CluStepEntity;
import net.mehvahdjukaar.tron_digitized.init.ClientSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CluStepEntityRenderer extends EntityRenderer<CluStepEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public CluStepEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    public ResourceLocation getTextureLocation(CluStepEntity entity) {
        return null;
    }

    public boolean shouldRender(CluStepEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(CluStepEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack poseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.getOrdinal() == 0) return;
        super.render(pEntity, pEntityYaw, pPartialTicks, poseStack, pBuffer, pPackedLight);
        var loc = ClientSetup.CLU_STEP;
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-pEntity.getYRot()));


        poseStack.translate(-0.5, -0.5+0.125, -0.5);
        TronBlockTileRenderer.renderBlockModel(loc, poseStack, pBuffer, blockRenderer, pPackedLight, OverlayTexture.NO_OVERLAY, false);
        poseStack.popPose();
    }
}
