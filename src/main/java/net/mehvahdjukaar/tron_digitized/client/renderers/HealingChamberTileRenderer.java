package net.mehvahdjukaar.tron_digitized.client.renderers;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.common.block.HealingChamberTile;
import net.mehvahdjukaar.tron_digitized.init.Locations;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import static net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS;


public class HealingChamberTileRenderer implements BlockEntityRenderer<HealingChamberTile> {

    //public static final ResourceLocation BOAT_MODEL = new ResourceLocation(Supplementaries.MOD_ID + ":block/jar_boat_ship");

    private final BlockRenderDispatcher blockRenderer;

    public HealingChamberTileRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();

    }

    public static final Material BUBBLE_BLOCK_MATERIAL = new Material(LOCATION_BLOCKS, Tron.res("blocks/glass_for_fireplace"));


    public static void blockVert(VertexConsumer builder, PoseStack matrixStackIn,
                                 float x, float y, float z,
                                 float r, float g, float b, float a,
                                 float u, float v,
                                 int lu, int lv,
                                 float nx, float ny, float nz) {
        builder.vertex(matrixStackIn.last().pose(), x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lu, lv)
                .normal(matrixStackIn.last().normal(), nx, ny, nz).endVertex();
    }

    @Override
    public void render(HealingChamberTile tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);

        int lu = light & '\uffff';
        int lv = light >> 16 & '\uffff';

        var model = blockRenderer.getBlockModelShaper().getModelManager()
                .getModel(Locations.HEALING_CHAMBER_GLASS);

        var quads = model.getQuads(ModRegistry.CLU_DOOR.get().defaultBlockState(), null, RandomSource.create());

        int formatLength = 8;

        {
            poseStack.pushPose();

            poseStack.scale(0.8f, 0.8f, 0.8f);

            poseStack.translate(0, 0.5, 0);

            {
                poseStack.translate(-0.5, -0.5, -0.5);


                var builder = buffer.getBuffer(RenderType.entityTranslucent(
                        Tron.res("textures/blocks/healing_organic.png")));


                float fade = Mth.lerp(partialTicks, tile.prevFade, tile.fade);
                for (var q : quads) {
                    int[] v = q.getVertices();

                    for (int i = 0; i < v.length / formatLength; i++) {

                        blockVert(builder, poseStack,
                                Float.intBitsToFloat(v[i * formatLength]),
                                Float.intBitsToFloat(v[i * formatLength + 1]),
                                Float.intBitsToFloat(v[i * formatLength + 2]),

                                1,
                                1f,
                                1,
                                0.1f + fade * 0.3f,

                                i % 2 == 0 ? 1 : 0,
                                i % 4 == 0 ? 1 : 0,

                                lu,
                                lv,

                                Float.intBitsToFloat(v[i * formatLength + 5]),
                                Float.intBitsToFloat(v[i * formatLength + 6]),
                                Float.intBitsToFloat(v[i * formatLength + 7]));
                    }


                }


            }
            poseStack.popPose();
        }

        {
            poseStack.pushPose();


            poseStack.translate(-0.5, -0.5, -0.5);


            var builder = buffer.getBuffer(RenderType.entityTranslucent(
                    Tron.res("textures/blocks/glass_for_fireplace.png")));

            for (var q : quads) {
                int[] v = q.getVertices();

                for (int i = 0; i < v.length / formatLength; i++) {

                    blockVert(builder, poseStack,
                            Float.intBitsToFloat(v[i * formatLength]),
                            Float.intBitsToFloat(v[i * formatLength + 1]),
                            Float.intBitsToFloat(v[i * formatLength + 2]),

                            0.83f,
                            0.98f,
                            1f,
                            0.90f,

                            i % 2 == 0 ? 1 : 0,
                            i % 4 == 0 ? 1 : 0,

                            lu,
                            lv,

                            Float.intBitsToFloat(v[i * formatLength + 5]),
                            Float.intBitsToFloat(v[i * formatLength + 6]),
                            Float.intBitsToFloat(v[i * formatLength + 7]));
                }


            }


            poseStack.popPose();
        }


        poseStack.popPose();

    }
}