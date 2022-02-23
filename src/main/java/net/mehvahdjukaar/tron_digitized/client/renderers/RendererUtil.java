package net.mehvahdjukaar.tron_digitized.client.renderers;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.mehvahdjukaar.tron_digitized.init.ClientSetup;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

import java.util.Random;

public class RendererUtil {
    //centered on x,z. aligned on y=0

    //stuff that falling sand uses. for some reason renderBlock doesn't use correct light level
    public static void renderBlockState(BlockState state, PoseStack matrixStack, MultiBufferSource buffer,
                                        BlockRenderDispatcher blockRenderer, Level world, BlockPos pos) {
        try {
            for (RenderType type : RenderType.chunkBufferLayers()) {
                if (ItemBlockRenderTypes.canRenderInLayer(state, type)) {
                    renderBlockState(state, matrixStack, buffer, blockRenderer, world, pos, type);
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static void renderBlockState(BlockState state, PoseStack matrixStack, MultiBufferSource buffer,
                                        BlockRenderDispatcher blockRenderer, Level world, BlockPos pos, RenderType type) {

        ForgeHooksClient.setRenderType(type);
        blockRenderer.getModelRenderer().tesselateBlock(world,
                blockRenderer.getBlockModel(state), state, pos, matrixStack,
                buffer.getBuffer(type), false, new Random(), 0,
                OverlayTexture.NO_OVERLAY);
        ForgeHooksClient.setRenderType(null);
    }

    //from resource location
    public static void renderBlockModel(ResourceLocation modelLocation, PoseStack matrixStack, MultiBufferSource buffer,
                                        BlockRenderDispatcher blockRenderer, int light, int overlay, boolean cutout) {

        blockRenderer.getModelRenderer().renderModel(matrixStack.last(),
                buffer.getBuffer(cutout ? Sheets.cutoutBlockSheet() : Sheets.solidBlockSheet()),
                null,
                blockRenderer.getBlockModelShaper().getModelManager().getModel(modelLocation),
                1.0F, 1.0F, 1.0F,
                light, overlay);
    }


    public static void addCube(VertexConsumer builder, PoseStack matrixStackIn, float w, float h, TextureAtlasSprite sprite, int combinedLightIn,
                               int color, float a, int combinedOverlayIn, boolean up, boolean down, boolean fakeshading, boolean flippedY) {
        addCube(builder, matrixStackIn, 0, 0, w, h, sprite, combinedLightIn, color, a, up, down, fakeshading, flippedY, false);
    }

    public static void addCube(VertexConsumer builder, PoseStack matrixStackIn, float uOff, float vOff, float w, float h, TextureAtlasSprite sprite, int combinedLightIn,
                               int color, float a, int combinedOverlayIn, boolean up, boolean down, boolean fakeshading, boolean flippedY) {
        addCube(builder, matrixStackIn, uOff, vOff, w, h, sprite, combinedLightIn, color, a, up, down, fakeshading, flippedY, false);
    }


    public static void addCube(VertexConsumer builder, PoseStack matrixStackIn, float uOff, float vOff, float w, float h, TextureAtlasSprite sprite, int combinedLightIn,
                               int color, float a, boolean up, boolean down, boolean fakeshading, boolean flippedY, boolean wrap) {
        int lu = combinedLightIn & '\uffff';
        int lv = combinedLightIn >> 16 & '\uffff'; // ok
        float atlasScaleU = sprite.getU1() - sprite.getU0();
        float atlasScaleV = sprite.getV1() - sprite.getV0();
        float minU = sprite.getU0() + atlasScaleU * uOff;
        float minV = sprite.getV0() + atlasScaleV * vOff;
        float maxU = minU + atlasScaleU * w;
        float maxV = minV + atlasScaleV * h;
        float maxV2 = minV + atlasScaleV * w;

        float r = (float) ((color >> 16 & 255)) / 255.0F;
        float g = (float) ((color >> 8 & 255)) / 255.0F;
        float b = (float) ((color & 255)) / 255.0F;


        // float a = 1f;// ((color >> 24) & 0xFF) / 255f;
        // shading:

        float r8, g8, b8, r6, g6, b6, r5, g5, b5;

        r8 = r6 = r5 = r;
        g8 = g6 = g5 = g;
        b8 = b6 = b5 = b;
        //TODO: make this affect uv values not rgb
        if (fakeshading) {
            float s1 = 0.8f, s2 = 0.6f, s3 = 0.5f;
            // 80%: s,n
            r8 *= s1;
            g8 *= s1;
            b8 *= s1;
            // 60%: e,w
            r6 *= s2;
            g6 *= s2;
            b6 *= s2;
            // 50%: d
            r5 *= s3;
            g5 *= s3;
            b5 *= s3;
            //100%

        }

        float hw = w / 2f;
        float hh = h / 2f;

        // up
        if (up)
            addQuadTop(builder, matrixStackIn, -hw, h, hw, hw, h, -hw, minU, minV, maxU, maxV2, r, g, b, a, lu, lv, 0, 1, 0);
        // down
        if (down)
            addQuadTop(builder, matrixStackIn, -hw, 0, -hw, hw, 0, hw, minU, minV, maxU, maxV2, r5, g5, b5, a, lu, lv, 0, -1, 0);


        if (flippedY) {
            float temp = minV;
            minV = maxV;
            maxV = temp;
        }
        float inc = 0;
        if (wrap) {
            inc = atlasScaleU * w;
        }

        // north z-
        // x y z u v r g b a lu lv
        addQuadSide(builder, matrixStackIn, hw, 0, -hw, -hw, h, -hw, minU, minV, maxU, maxV, r8, g8, b8, a, lu, lv, 0, 0, 1);
        // west
        addQuadSide(builder, matrixStackIn, -hw, 0, -hw, -hw, h, hw, minU + inc, minV, maxU + inc, maxV, r6, g6, b6, a, lu, lv, -1, 0, 0);
        // south
        addQuadSide(builder, matrixStackIn, -hw, 0, hw, hw, h, hw, minU + 2 * inc, minV, maxU + 2 * inc, maxV, r8, g8, b8, a, lu, lv, 0, 0, -1);
        // east
        addQuadSide(builder, matrixStackIn, hw, 0, hw, hw, h, -hw, minU + 3 * inc, minV, maxU + 3 * inc, maxV, r6, g6, b6, a, lu, lv, 1, 0, 0);
    }

    public static int setColorForAge(float age, float phase) {
        float a = (age + phase) % 1;
        float[] col = ColorHelper.getBubbleColor(a);
        ;
        return FastColor.ARGB32.color(255, (int) (col[0] * 255), (int) (col[1] * 255), (int) (col[2] * 255));
    }


    public static void renderBubble(VertexConsumer builder, PoseStack matrixStackIn, float w,
                                    TextureAtlasSprite sprite, int combinedLightIn,
                                    boolean flippedY, BlockPos pos, Level level, float partialTicks) {
        int lu = combinedLightIn & '\uffff';
        int lv = combinedLightIn >> 16 & '\uffff';
        float atlasScaleU = sprite.getU1() - sprite.getU0();
        float atlasScaleV = sprite.getV1() - sprite.getV0();
        float minU = sprite.getU0();
        float minV = sprite.getV0();
        float maxU = minU + atlasScaleU * w;
        float maxV = minV + atlasScaleV * w;
        float maxV2 = minV + atlasScaleV * w;

        long t = level == null ? System.currentTimeMillis() / 50 : level.getGameTime();
        float time = ((float) Math.floorMod((long) (pos.getX() * 7 + pos.getY() * 9 + pos.getZ() * 13) + t, 100L) + partialTicks) / 100.0F;

        float j = 0.5f + (Mth.sin((float) (time*Math.PI*2)))/4;

        int cUnw = setColorForAge(time, 0);
        int cUne = setColorForAge(time, 0.15f);
        int cUse = setColorForAge(time, 0.55f);
        int cUsw = setColorForAge(time, 0.35f);


        int cDnw = setColorForAge(time, 0.45f);

        int cDne = setColorForAge(time, 0.85f);

        int cDse = setColorForAge(time, 1);

        int cDsw = setColorForAge(time, 0.65f);


        //long time = System.currentTimeMillis();
        /*
        float unw = amp;
        float une = amp;
        float use = amp;
        float usw = amp;

        float dnw = use;
        float dne = usw;
        float dse = unw;
        float dsw = une;
        */
        var model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getModelManager()
                .getModel(ClientSetup.HEALING_CHAMBER_GLASS);

        var quads = model.getQuads(ModRegistry.CLU_DOOR.get().defaultBlockState(), null, new Random());
        BakedQuadBuilder quadBuilder = new BakedQuadBuilder(model.getParticleIcon());
        int formatLength = quadBuilder.getVertexFormat().getIntegerSize();

        for (var q : quads) {
            int[] v = q.getVertices();

            for (int i = 0; i < v.length / formatLength; i++) {

                blockVert(builder, matrixStackIn,
                        Float.intBitsToFloat(v[i*formatLength]),
                        Float.intBitsToFloat(v[i*formatLength + 1]),
                        Float.intBitsToFloat(v[i*formatLength + 2]),

                        1,
                        1,
                        1,
                        j,

                        i % 2 == 0 ? 1 : 0,
                        i % 4 == 0 ? 1 : 0,

                        lu,
                        lv,

                        Float.intBitsToFloat(v[i*formatLength + 5]),
                        Float.intBitsToFloat(v[i*formatLength + 6]),
                        Float.intBitsToFloat(v[i*formatLength + 7]));
            }


        }


/*
        addVert(builder, matrixStackIn,0.000000f, -1.500852f, -0.511262f, minU, maxV2, cUsw, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn, 0.000000f, -1.371012f, -0.641102f, maxU, maxV2, cUse, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn, 0.278164f, -1.371012f, -0.577613f, maxU, minV, cUne, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn,  0.221828f, -1.500852f, -0.460631f, minU, minV, cUnw, lu, lv, 0, 1, 0);



*/



/*

        v 0.000000f, 1.500853f, -0.511262f,
                v 0.000000f, 1.598545f, -0.355785f,
                v 0.154369f, 1.598545f, -0.320551f,
                v 0.221828f, 1.500853f, -0.460631f,

                v 0.000000f, -1.500852f, -0.511262f,
                v 0.000000f, -1.371012f, -0.641102f,
                v 0.278164f, -1.371012f, -0.577613f,
                v 0.221828f, -1.500852f, -0.460631f,

        v 0.625000 -1.467868 -0.366482
        v -0.625000 -1.467868 -0.366482
        v -0.625000 -0.843750 -0.625000
        v 0.625000 -0.843750 -0.625000
        v 0.625000 -1.467868 -0.366482
        v 0.625000 -0.843750 -0.625000
        v -0.625000 -1.467868 -0.366482
        v -0.625000 -0.843750 -0.625000


        addVert(builder, matrixStackIn,0.625058f, -0.843774f, -0.625000f, minU, maxV2, cUsw, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn, 0.366447f, -1.468114f, -0.625000f, maxU, maxV2, cUse, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn, 0.366447f, -1.468115f, 0.625000f, maxU, minV, cUne, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn, 0.625058f, -0.843774f, 0.625000f, minU, minV, cUnw, lu, lv, 0, 1, 0);
*/


        int l = 1;
        /*


         */

/*

        v -0.330550 -1.366374 -0.562500
        v -0.330550 -1.366374 -0.562500
        v -0.544614 -0.849578 -0.562500
        v -0.544614 -0.849578 -0.562500
        v -0.330550 -1.366374 0.562500
        v -0.544614 -0.849579 0.562500
        v -0.330550 -1.366374 0.562500
        v -0.544614 -0.849579 0.562500

        //addQuadTop(builder, matrixStackIn, -l+dx1, w, l, l, w, -l, minU, minV, maxU, maxV2, r, g, b, a, lu, lv, 0, 1, 0);
        //top
        addVert(builder, matrixStackIn, -l - usw, l + usw, l + usw, minU, maxV2, cUsw, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn, l + use, l + use, l + use, maxU, maxV2, cUse, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn, l + une, l + une, -l - une, maxU, minV, cUne, lu, lv, 0, 1, 0);
        addVert(builder, matrixStackIn, -l - unw, l + unw, -l - unw, minU, minV, cUnw, lu, lv, 0, 1, 0);


        //addQuadTop(builder, matrixStackIn, -l, 0, -l, l, 0, l, minU, minV, maxU, maxV2, r5, g5, b5, a, lu, lv, 0, -1, 0);

        addVert(builder, matrixStackIn, -l - dnw, -l - dnw, -l - dnw, minU, maxV2, cDnw, lu, lv, 0, -1, 0);
        addVert(builder, matrixStackIn, l + dne, -l - dne, -l - dne, maxU, maxV2, cDne, lu, lv, 0, -1, 0);
        addVert(builder, matrixStackIn, l + dse, -l - dse, l + dse, maxU, minV, cDse, lu, lv, 0, -1, 0);
        addVert(builder, matrixStackIn, -l - dsw, -l - dsw, l + dsw, minU, minV, cDsw, lu, lv, 0, -1, 0);

        if (flippedY) {
            float temp = minV;
            minV = maxV;
            maxV = temp;
        }

        // north z-
        // x y z u v r g b a lu lv
        //addQuadSide(builder, matrixStackIn, l, 0, -l, -l, w, -l, minU, minV, maxU, maxV, r8, g8, b8, a, lu, lv, 0, 0, 1);
        addVert(builder, matrixStackIn, l + dne, -l - dne, -l - dne, minU, maxV, cDne, lu, lv, 0, 0, 1);
        addVert(builder, matrixStackIn, -l - dnw, -l - dnw, -l - dnw, maxU, maxV, cDnw, lu, lv, 0, 0, 1);
        addVert(builder, matrixStackIn, -l - unw, l + unw, -l - unw, maxU, minV, cUnw, lu, lv, 0, 0, 1);
        addVert(builder, matrixStackIn, l + une, l + une, -l - une, minU, minV, cUne, lu, lv, 0, 0, 1);
        // west
        //addQuadSide(builder, matrixStackIn, -l, 0, -l, -l, w, l, minU, minV, maxU, maxV, r6, g6, b6, a, lu, lv, -1, 0, 0);
        addVert(builder, matrixStackIn, -l - dnw, -l - dnw, -l - dnw, minU, maxV, cDnw, lu, lv, -1, 0, 0);
        addVert(builder, matrixStackIn, -l - dsw, -l - dsw, l + dsw, maxU, maxV, cDsw, lu, lv, -1, 0, 0);
        addVert(builder, matrixStackIn, -l - usw, l + usw, l + usw, maxU, minV, cUsw, lu, lv, -1, 0, 0);
        addVert(builder, matrixStackIn, -l - unw, l + unw, -l - unw, minU, minV, cUnw, lu, lv, -1, 0, 0);
        // south
        //addQuadSide(builder, matrixStackIn, -l, 0, l, l, w, l, minU, minV, maxU, maxV, r8, g8, b8, a, lu, lv, 0, 0, -1);
        addVert(builder, matrixStackIn, -l - dsw, -l - dsw, l + dsw, minU, maxV, cDsw, lu, lv, 0, 0, -1);
        addVert(builder, matrixStackIn, l + dse, -l - dse, l + dse, maxU, maxV, cDse, lu, lv, 0, 0, -1);
        addVert(builder, matrixStackIn, l + use, l + use, l + use, maxU, minV, cUse, lu, lv, 0, 0, -1);
        addVert(builder, matrixStackIn, -l - usw, l + usw, l + usw, minU, minV, cUsw, lu, lv, 0, 0, -1);
        // east
        //addQuadSide(builder, matrixStackIn, l, 0, l, l, w, -l, minU, minV, maxU, maxV, r6, g6, b6, a, lu, lv, 1, 0, 0);
        addVert(builder, matrixStackIn, l + dse, -l - dse, l + dse, minU, maxV, cDse, lu, lv, 1, 0, 0);
        addVert(builder, matrixStackIn, l + dne, -l - dne, -l - dne, maxU, maxV, cDne, lu, lv, 1, 0, 0);
        addVert(builder, matrixStackIn, l + une, l + une, -l - une, maxU, minV, cUne, lu, lv, 1, 0, 0);
        addVert(builder, matrixStackIn, l + use, l + use, l + use, minU, minV, cUse, lu, lv, 1, 0, 0);
        */

    }

    public static void blockVert(VertexConsumer builder, PoseStack matrixStackIn,
                                 float x, float y, float z,
                                 float r, float g, float b, float a,
                                 float u, float v,
                                 int lu, int lv,
                                 float nx, float ny, float nz) {
        builder.vertex(matrixStackIn.last().pose(), x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lu, lv)
                .normal(matrixStackIn.last().normal(), nx, ny, nz).endVertex();
    }

    public static void addVert(VertexConsumer builder, PoseStack matrixStackIn, float x, float y, float z, float u, float v, int color, int lu, int lv, float nx, float ny, float nz) {
        builder.vertex(matrixStackIn.last().pose(), x, y, z).color(color).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lu, lv)
                .normal(matrixStackIn.last().normal(), nx, ny, nz).endVertex();
    }

    public static void addQuadSide(VertexConsumer builder, PoseStack matrixStackIn, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, float r, float g,
                                   float b, float a, int lu, int lv, float nx, float ny, float nz) {
        addVert(builder, matrixStackIn, x0, y0, z0, u0, v1, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x1, y0, z1, u1, v1, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x1, y1, z1, u1, v0, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x0, y1, z0, u0, v0, r, g, b, a, lu, lv, nx, ny, nz);
    }

    public static void addQuadSide(VertexConsumer builder, PoseStack matrixStackIn, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, float r, float g,
                                   float b, float a, int lu, int lv, float nx, float ny, float nz, TextureAtlasSprite sprite) {

        u0 = getRelativeU(sprite, u0);
        u1 = getRelativeU(sprite, u1);
        v0 = getRelativeV(sprite, v0);
        v1 = getRelativeV(sprite, v1);

        addVert(builder, matrixStackIn, x0, y0, z0, u0, v1, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x1, y0, z1, u1, v1, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x1, y1, z1, u1, v0, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x0, y1, z0, u0, v0, r, g, b, a, lu, lv, nx, ny, nz);
    }

    public static void addQuadTop(VertexConsumer builder, PoseStack matrixStackIn, float x0, float y0, float z0, float x1, float y1, float z1, float u0, float v0, float u1, float v1, float r, float g,
                                  float b, float a, int lu, int lv, float nx, float ny, float nz) {
        addVert(builder, matrixStackIn, x0, y0, z0, u0, v1, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x1, y0, z0, u1, v1, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x1, y1, z1, u1, v0, r, g, b, a, lu, lv, nx, ny, nz);
        addVert(builder, matrixStackIn, x0, y1, z1, u0, v0, r, g, b, a, lu, lv, nx, ny, nz);
    }


    public static void addVert(VertexConsumer builder, PoseStack matrixStackIn, float x, float y, float z, float u, float v, float r, float g,
                               float b, float a, int lu, int lv, float nx, float ny, float nz) {
        builder.vertex(matrixStackIn.last().pose(), x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lu, lv)
                .normal(matrixStackIn.last().normal(), nx, ny, nz).endVertex();
    }

    public static void addVert(VertexConsumer builder, PoseStack matrixStackIn, float x, float y, float z, float u, float v, float r, float g,
                               float b, float a, int lu, int lv, float nx, float ny, float nz, TextureAtlasSprite sprite) {
        builder.vertex(matrixStackIn.last().pose(), x, y, z).color(r, g, b, a).uv(getRelativeU(sprite, u), getRelativeV(sprite, v))
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(lu, lv).normal(matrixStackIn.last().normal(), nx, ny, nz).endVertex();
    }

    public static float getRelativeU(TextureAtlasSprite sprite, float u) {
        float f = sprite.getU1() - sprite.getU0();
        return sprite.getU0() + f * u;
    }

    public static float getRelativeV(TextureAtlasSprite sprite, float v) {
        float f = sprite.getV1() - sprite.getV0();
        return sprite.getV0() + f * v;
    }


}
