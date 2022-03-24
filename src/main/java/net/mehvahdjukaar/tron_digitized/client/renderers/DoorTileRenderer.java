package net.mehvahdjukaar.tron_digitized.client.renderers;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.common.block.DoorBlock;
import net.mehvahdjukaar.tron_digitized.common.block.DoorBlockTile;
import net.mehvahdjukaar.tron_digitized.init.Locations;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;


public class DoorTileRenderer extends TronBlockTileRenderer<DoorBlockTile> {

    //public static final ResourceLocation BOAT_MODEL = new ResourceLocation(Supplementaries.MOD_ID + ":block/jar_boat_ship");

    private final BlockRenderDispatcher blockRenderer;

    public DoorTileRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
    }


    @Override
    public void render(DoorBlockTile tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int light, int overlay) {

        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);


        if (tile.getBlockState().getValue(DoorBlock.FACING).getAxis() != Direction.Axis.X) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        }

        poseStack.pushPose();
        poseStack.translate(-0.5, -0.5, -0.5);
        renderBlockModel(tile.getModelLocation(), poseStack, buffer, blockRenderer, light, overlay, tile.isTranslucent());

        poseStack.popPose();

        float progress = Mth.lerp(partialTicks, tile.prevOpeningProgress, tile.openingProgress) * 1.35f;

        if(tile.getBlockState().getBlock() == ModRegistry.CLU_DOOR.get()) {
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
        }else{
            poseStack.translate(0, 0, -progress);
            poseStack.translate(-0.5, 0, -0.5);
            renderBlockModel(Locations.WHITE_DOOR, poseStack, buffer, blockRenderer, light, overlay, tile.isTranslucent());
        }


        poseStack.popPose();

    }
}