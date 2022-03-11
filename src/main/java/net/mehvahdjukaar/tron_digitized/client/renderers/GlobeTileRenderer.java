package net.mehvahdjukaar.tron_digitized.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.block.GlobeBlockTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;

public class GlobeTileRenderer extends TronBlockTileRenderer<GlobeBlockTile> {
    public GlobeTileRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(GlobeBlockTile tile, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        pPoseStack.pushPose();
        pPoseStack.translate(0.5,0.5,0.5);
        //float t = ((System.currentTimeMillis() % 360000) / 100f);
        long i = tile.getLevel().getGameTime();
        int scale = 377;
        float f2 = ((float)Math.floorMod(i, (long)scale) + pPartialTick) / (float)scale;
        //float t = (tile.getLevel().getGameTime() % 360) * (float) 2.4 + pPartialTick ;
        pPoseStack.mulPose(Vector3f.YN.rotationDegrees(f2*360));
        pPoseStack.translate(-0.5,-0.5,-0.5);
        super.render(tile, pPartialTick, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay);
        pPoseStack.popPose();
    }
}
