package net.mehvahdjukaar.tron_digitized.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.tron_digitized.Tron;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.BiFunction;

public class DigitizedRenderType extends RenderType {

    public DigitizedRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    protected static final TexturingStateShard TEXTURING_STATE_SHARD = new TexturingStateShard("entity_glint_texturing",
            () -> addTextureMovement(1.2F, 4L), RenderSystem::resetTextureMatrix);

    protected static final TextureStateShard TEXTURE_SHARD = new TextureStateShard(Tron.res("textures/entity/healing_player.png"), true, false);

    public static final RenderType DIGITIZED_GLINT =
            create("digitized", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,
                    true, true, CompositeState.builder()
                            .setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER)
                            .setTextureState(TEXTURE_SHARD)
                            .setWriteMaskState(COLOR_DEPTH_WRITE)
                            .setCullState(NO_CULL)
                            .setDepthTestState(EQUAL_DEPTH_TEST)
                            .setTexturingState(TEXTURING_STATE_SHARD)
                            .setOverlayState(OVERLAY).createCompositeState(true));

    public static RenderType test2(){

        return create("digitized3", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256,
                true, true, RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(TEXTURE_SHARD)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .setTexturingState(TEXTURING_STATE_SHARD)
                        .setDepthTestState(EQUAL_DEPTH_TEST)

                        .createCompositeState(true));
    };

    private static void addTextureMovement(float in, long time) {

        long i = Util.getMillis() * time;
        float f = (float) (i % 80000L) / 80000.0F;
        float f1 = 0.5f + Mth.sin((float) (((float) (i % 30000L) / 30000.0F) * Math.PI)) * 0.5f;
        Matrix4f matrix4f = Matrix4f.createTranslateMatrix(0.0F, f, 0.0F);
        matrix4f.multiply(Vector3f.ZP.rotationDegrees(30));
        float scale = 1.5f;
        matrix4f.multiply(Matrix4f.createScaleMatrix(scale, scale, scale));

        RenderSystem.setTextureMatrix(matrix4f);


    }

}
