package net.mehvahdjukaar.tron_digitized.client.renderers;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.block.TronBlockTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.Vec3;

public class TronBlockTileRenderer<T extends TronBlockTile> implements BlockEntityRenderer<T> {

    private final BlockRenderDispatcher blockRenderer;

    public TronBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();

    }

    @Override
    public boolean shouldRenderOffScreen(T pBlockEntity) {
        return true;
    }

    @Override
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        var loc = pBlockEntity.getModelLocation();
        if (loc != null) {
            pPoseStack.translate(0.5, 0.5, 0.5);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(-pBlockEntity.getYaw()));
            pPoseStack.translate(-0.5, -0.5, -0.5);
            renderBlockModel(loc, pPoseStack, pBufferSource, blockRenderer, pPackedLight, pPackedOverlay, pBlockEntity.isTranslucent());
        }
    }

    public static void renderBlockModel(ResourceLocation modelLocation, PoseStack matrixStack, MultiBufferSource buffer,
                                        BlockRenderDispatcher blockRenderer, int light, int overlay, boolean translucent) {

        blockRenderer.getModelRenderer().renderModel(matrixStack.last(),
                buffer.getBuffer(translucent ? Sheets.translucentCullBlockSheet() : Sheets.cutoutBlockSheet()),
                null,
                blockRenderer.getBlockModelShaper().getModelManager().getModel(modelLocation),
                1.0F, 1.0F, 1.0F,
                light, overlay);
    }
}
