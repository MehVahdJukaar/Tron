package net.mehvahdjukaar.tron_digitized.init;

import com.google.common.collect.ImmutableList;
import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.client.DigitizedGuiOverlay;
import net.mehvahdjukaar.tron_digitized.client.DigitizedLayer;
import net.mehvahdjukaar.tron_digitized.client.renderers.ChairEntityRenderer;
import net.mehvahdjukaar.tron_digitized.client.renderers.CluDoorTileRenderer;
import net.mehvahdjukaar.tron_digitized.client.renderers.TronBlockTileRenderer;
import net.mehvahdjukaar.tron_digitized.client.renderers.HealingChamberTileRenderer;
import net.mehvahdjukaar.tron_digitized.common.entity.IHealableEntity;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Tron.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static final ResourceLocation CLU_DOOR_LEFT = new ResourceLocation(Tron.MOD_ID + ":block/clu_door_left");
    public static final ResourceLocation CLU_DOOR_RIGHT = new ResourceLocation(Tron.MOD_ID + ":block/clu_door_right");
    public static final ResourceLocation HEALING_CHAMBER_GLASS = new ResourceLocation(Tron.MOD_ID + ":block/healing_chamber_glass");
    public static final ResourceLocation BLACK_CHAIR = new ResourceLocation(Tron.MOD_ID + ":block/black_chair");
    public static final ResourceLocation WHITE_CHAIR = new ResourceLocation(Tron.MOD_ID + ":block/white_chair");
    public static final ResourceLocation RECLINER = new ResourceLocation(Tron.MOD_ID + ":block/recliner");
    public static final ResourceLocation KITCHEN_CHAIR = new ResourceLocation(Tron.MOD_ID + ":block/kitchen_chair");
    public static final ResourceLocation CLU_THRONE = new ResourceLocation(Tron.MOD_ID + ":block/clu_throne");
    public static final ResourceLocation CLU_THRONE_BIG = new ResourceLocation(Tron.MOD_ID + ":block/clu_throne_big");


    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        //loaders
        // ModelLoaderRegistry.registerLoader(Tron.res("glow_lights_loader"), new GlowLightsModelLoader());
        ForgeModelBakery.addSpecialModel(CLU_DOOR_LEFT);
        ForgeModelBakery.addSpecialModel(CLU_DOOR_RIGHT);
        ForgeModelBakery.addSpecialModel(HEALING_CHAMBER_GLASS);
        ForgeModelBakery.addSpecialModel(BLACK_CHAIR);
        ForgeModelBakery.addSpecialModel(RECLINER);
        ForgeModelBakery.addSpecialModel(KITCHEN_CHAIR);
        ForgeModelBakery.addSpecialModel(WHITE_CHAIR);
        ForgeModelBakery.addSpecialModel(CLU_THRONE);
        ForgeModelBakery.addSpecialModel(CLU_THRONE_BIG);
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModRegistry.CLU_DOOR_TILE.get(), CluDoorTileRenderer::new);
        event.registerBlockEntityRenderer(ModRegistry.HEALING_CHAMBER_TILE.get(), HealingChamberTileRenderer::new);
        event.registerBlockEntityRenderer(ModRegistry.CUSTOM_BLOCK_TILE.get(), TronBlockTileRenderer::new);

        event.registerEntityRenderer(ModRegistry.CHAIR_ENTITY.get(), ChairEntityRenderer::new);
    }

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.HEALING_CHAMBER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.BLACK_CHAIR.get(), RenderType.translucent());
        DigitizedGuiOverlay.register();
    }


    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        BlockColors colors = event.getBlockColors();

    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        //adds to all entities
        var entityTypes = getEntities();
        entityTypes.forEach((entityType -> addLayer(event.getRenderer(entityType))));

        //player skins
        for (String skinType : event.getSkins()){
            var renderer = event.getSkin(skinType);
            addPlayerLayer(renderer);
        }
    }

    private static <T extends LivingEntity & IHealableEntity> List<EntityType<T>> getEntities(){
        return ImmutableList.copyOf(
                ForgeRegistries.ENTITIES.getValues().stream()
                        //only living entities have attributes
                        .filter(DefaultAttributes::hasSupplier)
                        .filter(e-> (e != EntityType.ENDER_DRAGON))
                        .map(entityType -> (EntityType<T>) entityType)
                        .collect(Collectors.toList()));
    }

    private static <M extends EntityModel<? extends Player>, R extends LivingEntityRenderer<? extends Player, M>> void addPlayerLayer(@Nullable R renderer){
        if(renderer != null) {
            renderer.addLayer(new DigitizedLayer(renderer));
        }
    }

    private static <T extends LivingEntity & IHealableEntity, M extends EntityModel<T>, R extends LivingEntityRenderer<T, M>> void addLayer(@Nullable R renderer){
        if(renderer != null) {
            renderer.addLayer(new DigitizedLayer<>(renderer));
        }
    }

}
