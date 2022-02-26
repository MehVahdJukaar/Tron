package net.mehvahdjukaar.tron_digitized.init;

import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.common.block.*;
import net.mehvahdjukaar.tron_digitized.common.entity.ChairEntity;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ModRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Tron.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Tron.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Tron.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Tron.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Tron.MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Tron.MOD_ID);

    public static void init(IEventBus bus) {
        SOUNDS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILES.register(bus);
        ENTITIES.register(bus);
        PARTICLES.register(bus);
    }

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("tron_digitized"){
        @Override
        public ItemStack makeIcon() {
            return Items.DIAMOND_BLOCK.getDefaultInstance();
        }
    };

    private static RegistryObject<Item> regItem(String name, Supplier<? extends Item> sup) {
        return ITEMS.register(name, sup);
    }

    protected static RegistryObject<Item> regBlockItem(RegistryObject<Block> blockSup) {
        return regItem(blockSup.getId().getPath(), () -> new BlockItem(blockSup.get(), (new Item.Properties()).tab(MOD_TAB)));
    }

    public static RegistryObject<SoundEvent> makeSoundEvent(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(Tron.res(name)));
    }
    protected static RegistryObject<Block> regWithItem(String name, Supplier<Block> blockSup) {
        var b =  BLOCKS.register(name, blockSup);
        regBlockItem(b);
        return b;
    }

    private static final Set<Supplier<Block>> TILE_BLOCKS = new HashSet<>();

    public static RegistryObject<Block> regTileBlock(String name, Supplier<Block> blockSup){
        var b = regWithItem(name, blockSup);
        TILE_BLOCKS.add(b);
        return b;
    }

    private static Block[] getAllTileBlocks(){
        return TILE_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new);
    }

    public static final RegistryObject<Block> CLU_DOOR = regWithItem("clu_door", () ->
            new CluDoorBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_WHITE)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(1.5F)));

    public static final RegistryObject<BlockEntityType<CluDoorBlockTile>> CLU_DOOR_TILE = TILES
            .register("clu_door", () -> BlockEntityType.Builder.of(CluDoorBlockTile::new,
                    ModRegistry.CLU_DOOR.get()).build(null));

    public static final RegistryObject<Block> HEALING_CHAMBER = regWithItem("healing_chamber", () ->
            new HealingChamberBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_WHITE)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(1.5F)));

    public static final RegistryObject<BlockEntityType<HealingChamberTile>> HEALING_CHAMBER_TILE = TILES
            .register("healing_chamber", () -> BlockEntityType.Builder.of(HealingChamberTile::new,
                    ModRegistry.HEALING_CHAMBER.get()).build(null));


    public static final RegistryObject<SoundEvent> CLU_DOOR_SOUND = makeSoundEvent("block.clu_door");
    public static final RegistryObject<SoundEvent> HEALING_CHAMBER_SOUND = makeSoundEvent("block.healing_chamber");


    public static final RegistryObject<Block> BLACK_CHAIR = regTileBlock("black_chair", () ->
            new ChairBlock(BlockBehaviour.Properties.of(Material.GLASS, MaterialColor.COLOR_BLACK)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .strength(1),ClientSetup.BLACK_CHAIR,32,24,13));

    public static final RegistryObject<Block> KITCHEN_CHAIR = regTileBlock("kitchen_chair", () ->
            new ChairBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1), ClientSetup.KITCHEN_CHAIR, 36, 18, 16f));

    public static final RegistryObject<Block> THRONE = regTileBlock("clu_throne", () ->
            new ChairBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1),ClientSetup.CLU_THRONE,28, 48,18,16));

    public static final RegistryObject<Block> WHITE_CHAIR = regTileBlock("white_chair", () ->
            new TronBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1),ClientSetup.WHITE_CHAIR, 16, 16));

    public static final RegistryObject<Block> RECLINER = regTileBlock("recliner", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.RECLINER, 11, 18, 32));

    public static final RegistryObject<Block> BEDSIDE = regTileBlock("bedside", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.BEDSIDE, 11, 24, 14));





    //generic tile for entity models (For translucent stuff)
    public static final RegistryObject<BlockEntityType<TronBlockTile>> CUSTOM_BLOCK_TILE = TILES
            .register("custom_block_tile", () -> BlockEntityType.Builder.of(TronBlockTile::new,
                    getAllTileBlocks()
            ).build(null));


    public static final RegistryObject<EntityType<ChairEntity>> CHAIR_ENTITY = ENTITIES.register("chair",
            () -> EntityType.Builder.<ChairEntity>of(ChairEntity::new, MobCategory.MISC)
                    .sized(0.375F, 0.5F)
                    .updateInterval(-1)
                    .clientTrackingRange(3)
                    .setShouldReceiveVelocityUpdates(false)
                    .build("chair"));


   /*


    public static final RegistryObject<EntityType<ContainerHolderEntity>> CONTAINER_ENTITY = ENTITIES.register("container_entity",
            () -> EntityType.Builder.of(ContainerHolderEntity::new, MobCategory.MISC)
                    .sized(0.75f, 0.75f)
                    .clientTrackingRange(8)
                    .build("container_entity"));


    public static final Map<Boat.Type, RegistryObject<Item>> SLED_ITEMS = Stream.of(Boat.Type.values()).collect(ImmutableMap.toImmutableMap((e) -> e,
            (t) -> regItem("sled_" + t.getName(), () -> new SledItem(t))));

    public static final RegistryObject<Block> CANDY_CANE_BLOCK = BLOCKS.register("candy_cane_block", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_PINK)
                    .requiresCorrectToolForDrops().strength(1.5F).sound(SoundType.CALCITE)));
    public static final RegistryObject<Item> CANDY_CANE_BLOCK_ITEM = regBlockItem(CANDY_CANE_BLOCK, CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Item> CANDY_CANE = regItem("candy_cane",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties
                    .Builder().nutrition(2).saturationMod(0.4f).build())));

    public static final RegistryObject<Item> GINGERBREAD_COOKIE = regItem("gingerbread_cookie",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties
                    .Builder().nutrition(1).fast().saturationMod(0.4f).build())));

    public static final RegistryObject<Item> EGGNOG = regItem("eggnog", EggnogItem::new);

    public static final RegistryObject<Item> WINTER_DISC = ITEMS.register("winter_disc",
            () -> new RecordItem(14, WINTER_MUSIC, new Item.Properties()
                    .tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE).stacksTo(1)));

    public static final RegistryObject<Block> GINGERBREAD_BLOCK = BLOCKS.register("gingerbread", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_ORANGE)
                    .sound(SoundType.WOOD).strength(1F)));
    public static final RegistryObject<Item> GINGERBREAD_BLOCK_ITEM = regBlockItem(GINGERBREAD_BLOCK, CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> GINGERBREAD_FROSTED_BLOCK = BLOCKS.register("gingerbread_frosted", () ->
            new Block(BlockBehaviour.Properties.copy(GINGERBREAD_BLOCK.get())));
    public static final RegistryObject<Item> GINGERBREAD_FROSTED_BLOCK_ITEM = regBlockItem(GINGERBREAD_FROSTED_BLOCK, CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> GINGERBREAD_DOOR = BLOCKS.register("gingerbread_door", () ->
            new DoorBlock(BlockBehaviour.Properties.copy(GINGERBREAD_BLOCK.get())));
    public static final RegistryObject<Item> GINGERBREAD_DOOR_ITEM = regBlockItem(GINGERBREAD_DOOR, CreativeModeTab.TAB_REDSTONE);

    public static final RegistryObject<Block> GINGERBREAD_TRAPDOOR = BLOCKS.register("gingerbread_trapdoor", () ->
            new TrapDoorBlock(BlockBehaviour.Properties.copy(GINGERBREAD_BLOCK.get())));
    public static final RegistryObject<Item> GINGERBREAD_TRAPDOOR_ITEM = regBlockItem(GINGERBREAD_TRAPDOOR, CreativeModeTab.TAB_REDSTONE);


    public static final RegistryObject<Block> GINGER_WILD = BLOCKS.register("wild_ginger", () -> new WildGingerBlock(
            BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));
    public static final RegistryObject<Item> GINGER_WILD_ITEM = regBlockItem(GINGER_WILD, CreativeModeTab.TAB_DECORATIONS);


    public static final RegistryObject<Block> GINGER_CROP = BLOCKS.register("ginger", () ->
            new GingerBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)));
    public static final RegistryObject<Item> GINGER_FLOWER = regItem("ginger_flower",
            () -> new ItemNameBlockItem(GINGER_CROP.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> GINGER = regItem("ginger",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD)));


    public static final Map<DyeColor, RegistryObject<Block>> GUMDROPS_BUTTONS = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Item>> GUMDROPS_BUTTON_ITEMS = new HashMap<>();

    public static final Map<DyeColor, RegistryObject<Block>> GLOW_LIGHTS_BLOCKS = new HashMap<>();
    public static final Map<DyeColor, RegistryObject<Item>> GLOW_LIGHTS_ITEMS = new HashMap<>();


    public static final RegistryObject<BlockEntityType<GlowLightsBlockTile>> GLOW_LIGHTS_BLOCK_TILE = TILES
            .register("glow_lights", () -> BlockEntityType.Builder.of(GlowLightsBlockTile::new,
                    ModRegistry.GLOW_LIGHTS_BLOCKS.values().stream().map(RegistryObject::get).toArray(Block[]::new)).build(null));


    public static final RegistryObject<Block> WREATH = BLOCKS.register("wreath", () ->
            new WreathBlock(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_GREEN)
                    .sound(SoundType.VINE).strength(0.1f).noCollission()));
    public static final RegistryObject<Item> WREATH_ITEM = regBlockItem(WREATH, CreativeModeTab.TAB_DECORATIONS);
*/

}
