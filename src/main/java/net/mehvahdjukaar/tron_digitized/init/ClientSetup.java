package net.mehvahdjukaar.tron_digitized.init;

import com.google.common.collect.ImmutableList;
import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.client.DigitizedGuiOverlay;
import net.mehvahdjukaar.tron_digitized.client.DigitizedLayer;
import net.mehvahdjukaar.tron_digitized.client.renderers.*;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Tron.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    private static final Set<ResourceLocation> CUSTOM_MODELS = new HashSet<>();

    public static final ResourceLocation CLU_DOOR = addCustomModel("clu_door");
    public static final ResourceLocation CLU_DOOR_LEFT = addCustomModel("clu_door_left");
    public static final ResourceLocation CLU_DOOR_RIGHT = addCustomModel("clu_door_right");
    public static final ResourceLocation HEALING_CHAMBER_GLASS = addCustomModel("healing_chamber_glass");
    public static final ResourceLocation BLACK_CHAIR = addCustomModel("black_chair");
    public static final ResourceLocation WHITE_CHAIR = addCustomModel("white_chair");
    public static final ResourceLocation RECLINER = addCustomModel("recliner");
    public static final ResourceLocation KITCHEN_CHAIR = addCustomModel("kitchen_chair");
    public static final ResourceLocation CLU_THRONE = addCustomModel("clu_throne");
    public static final ResourceLocation BEDSIDE = addCustomModel("bedside");
    public static final ResourceLocation KITCHEN_TABLE = addCustomModel("kitchen_table");
    public static final ResourceLocation METAL_TABLE = addCustomModel("metal_table");
    public static final ResourceLocation QUORRAS_DRESSER = addCustomModel("quorras_dresser");
    public static final ResourceLocation SIMPLE_GLASS_TABLE = addCustomModel("simple_glass_table");
    public static final ResourceLocation WHITE_TABLE = addCustomModel("white_table");
    public static final ResourceLocation BOOKSHELF = addCustomModel("bookshelf");
    public static final ResourceLocation BLACK_COUCH = addCustomModel("black_couch");
    public static final ResourceLocation CLU_STEP = addCustomModel("clu_step");
    public static final ResourceLocation CLU_STAIRS_FILL = addCustomModel("clu_stairs_fill");
    public static final ResourceLocation BOOKSHELF2 = addCustomModel("bookshelf2");
    public static final ResourceLocation BOOKSHELF3 = addCustomModel("bookshelf3");
    public static final ResourceLocation BOOKSHELF_END = addCustomModel("bookshelf_end");
    public static final ResourceLocation BOOKSHELF_END2 = addCustomModel("bookshelf_end2");
    public static final ResourceLocation FLYNN_LAMP = addCustomModel("flynn_lamp");
    public static final ResourceLocation FLYNN_LAMP_OFF = addCustomModel("flynn_lamp_off");
    public static final ResourceLocation CLU_BACK_DOOR = addCustomModel("clu_back_door");
    public static final ResourceLocation CLU_BACK_STAIRS = addCustomModel("clu_back_stairs");

    public static ResourceLocation addCustomModel(String name){
        var res =  new ResourceLocation(Tron.MOD_ID + ":block/"+name);
        CUSTOM_MODELS.add(res);
        return res;
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        //loaders
        CUSTOM_MODELS.forEach(ForgeModelBakery::addSpecialModel);
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModRegistry.CLU_DOOR_TILE.get(), CluDoorTileRenderer::new);
        event.registerBlockEntityRenderer(ModRegistry.HEALING_CHAMBER_TILE.get(), HealingChamberTileRenderer::new);
        event.registerBlockEntityRenderer(ModRegistry.CUSTOM_BLOCK_TILE.get(), TronBlockTileRenderer::new);

        event.registerEntityRenderer(ModRegistry.CHAIR_ENTITY.get(), ChairEntityRenderer::new);
        event.registerEntityRenderer(ModRegistry.CLU_STEP_ENTITY.get(), CluStepEntityRenderer::new);
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
