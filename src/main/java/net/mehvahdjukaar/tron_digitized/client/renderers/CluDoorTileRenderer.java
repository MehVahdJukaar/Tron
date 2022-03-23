package net.mehvahdjukaar.tron_digitized.client.renderers;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.block.CluDoorBlock;
import net.mehvahdjukaar.tron_digitized.common.block.CluDoorBlockTile;
import net.mehvahdjukaar.tron_digitized.init.Locations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;


public class CluDoorTileRenderer extends TronBlockTileRenderer<CluDoorBlockTile> {

    //public static final ResourceLocation BOAT_MODEL = new ResourceLocation(Supplementaries.MOD_ID + ":block/jar_boat_ship");

    private final BlockRenderDispatcher blockRenderer;

    public CluDoorTileRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
    }


    @Override
    public void render(CluDoorBlockTile tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int light, int overlay) {

        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);


        if (tile.getBlockState().getValue(CluDoorBlock.AXIS) != Direction.Axis.X) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        }

        poseStack.pushPose();
        poseStack.translate(-0.5, -0.5, -0.5);
        renderBlockModel(tile.getModelLocation(), poseStack, buffer, blockRenderer, light, overlay, tile.isTranslucent());

        poseStack.popPose();

        float progress = Mth.lerp(partialTicks, tile.prevOpeningProgress, tile.openingProgress) * 1.35f;

        poseStack.pushPose();

        poseStack.translate(0, 0, -progress);
        poseStack.translate(-0.5, 0, -0.5);
        renderBlockModel(Locations.CLU_DOOR_LEFT, poseStack, buffer, blockRenderer, light, overlay, tile.isTranslucent());

        poseStack.popPose();


        poseStack.pushPose();

        poseStack.translate(0, 0, progress);
        poseStack.translate(-0.5, 0, -0.5);
        renderBlockModel(Locations.CLU_DOOR_RIGHT, poseStack, buffer, blockRenderer, light, overlay, tile.isTranslucent());

        poseStack.popPose();


        poseStack.popPose();

    }
}