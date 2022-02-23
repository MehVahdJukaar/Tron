package net.mehvahdjukaar.tron_digitized.client.renderers;

import net.mehvahdjukaar.tron_digitized.common.entity.ChairEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ChairEntityRenderer extends EntityRenderer<ChairEntity> {
    public ChairEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(ChairEntity entity) {
        return null;
    }

    public boolean shouldRender(ChairEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        return false;
    }
}
