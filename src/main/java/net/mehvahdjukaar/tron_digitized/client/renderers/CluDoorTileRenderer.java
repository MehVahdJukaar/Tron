package net.mehvahdjukaar.tron_digitized.client.renderers;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.block.CluDoorBlock;
import net.mehvahdjukaar.tron_digitized.common.block.CluDoorBlockTile;
import net.mehvahdjukaar.tron_digitized.init.ClientSetup;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;


public class CluDoorTileRenderer implements BlockEntityRenderer<CluDoorBlockTile> {

    //public static final ResourceLocation BOAT_MODEL = new ResourceLocation(Supplementaries.MOD_ID + ":block/jar_boat_ship");

    private final BlockRenderDispatcher blockRenderer;

    public CluDoorTileRenderer(BlockEntityRendererProvider.Context context) {
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

        float progress = Mth.lerp(partialTicks, tile.prevOpeningProgress, tile.openingProgress)*1.35f;

        poseStack.pushPose();

        poseStack.translate(0, 0, -progress);
        poseStack.translate(-0.5, 0, -0.5);


        blockRenderer.getModelRenderer().renderModel(poseStack.last(),
                buffer.getBuffer(Sheets.solidBlockSheet()),
                null,
                blockRenderer.getBlockModelShaper().getModelManager().getModel(ClientSetup.CLU_DOOR_LEFT),
                1.0F, 1.0F, 1.0F,
                light, overlay);

        poseStack.popPose();

        poseStack.pushPose();

        poseStack.translate(0, 0, progress);
        poseStack.translate(-0.5, 0, -0.5);


        blockRenderer.getModelRenderer().renderModel(poseStack.last(),
                buffer.getBuffer(Sheets.solidBlockSheet()),
                null,
                blockRenderer.getBlockModelShaper().getModelManager().getModel(ClientSetup.CLU_DOOR_RIGHT),
                1.0F, 1.0F, 1.0F,
                light, overlay);

        poseStack.popPose();


        poseStack.popPose();

    }
}