package net.mehvahdjukaar.tron_digitized.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.block.GlobeBlockTile;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GlobeTileRenderer extends TronBlockTileRenderer<GlobeBlockTile> {
    public GlobeTileRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GlobeBlockTile tile, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        var loc = tile.getModelLocation();
        if (loc != null) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5, 0.5, 0.5);
            // int s = 10000;
            //float t = ((System.currentTimeMillis() % (s*10000)) / (float)s)*360;
            long i = tile.getLevel().getGameTime();
            int scale = 377;
            float f2 = ((float) Math.floorMod(i, (long) scale) + pPartialTick) / (float) scale;
            //float t = (tile.getLevel().getGameTime() % 360) * (float) 2.4 + pPartialTick ;
            pPoseStack.mulPose(Vector3f.YN.rotation((float) (f2 * 2 * Math.PI)));
            pPoseStack.translate(-0.5, -0.5, -0.5);


          renderBlockModel(loc, pPoseStack, pBufferSource, blockRenderer, pPackedLight, pPackedOverlay, true);

            pPoseStack.popPose();
        }

    }
}
