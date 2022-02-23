package net.mehvahdjukaar.tron_digitized.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.tron_digitized.common.entity.IHealableEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class DigitizedLayer<T extends LivingEntity & IHealableEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public DigitizedLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if(entity.isInHealingChamber()) {

            VertexConsumer buffer = bufferIn.getBuffer(DigitizedRenderType.DIGITIZED_GLINT);


            int i = entity.tickCount;
            float f = (((float) (i % 2000L) + partialTicks) / 20000.0F);
            float f1 = 0.5f + Mth.sin((float) (f * Math.PI)) * 0.3f;

            float alpha = f1;
            matrixStackIn.pushPose();
            this.getParentModel().renderToBuffer(matrixStackIn, buffer, packedLightIn,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1, 1.0F, alpha);
            matrixStackIn.popPose();
        }


    }
}
