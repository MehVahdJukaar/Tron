package net.mehvahdjukaar.tron_digitized.init;

import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.common.block.*;
import net.mehvahdjukaar.tron_digitized.common.entity.ChairEntity;
import net.mehvahdjukaar.tron_digitized.common.entity.CluStepEntity;
import net.mehvahdjukaar.tron_digitized.common.entity.ScreenEntity;
import net.mehvahdjukaar.tron_digitized.common.items.ScreenItem;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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

    public static final CreativeModeTab MOD_TAB = new CreativeModeTab("tron_digitized") {
        @Override
        public ItemStack makeIcon() {
            return CLU_DOOR.get().asItem().getDefaultInstance();
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
        var b = BLOCKS.register(name, blockSup);
        regBlockItem(b);
        return b;
    }

    private static final Set<Supplier<Block>> TILE_BLOCKS = new HashSet<>();

    public static RegistryObject<Block> regTileBlock(String name, Supplier<Block> blockSup) {
        var b = regWithItem(name, blockSup);
        TILE_BLOCKS.add(b);
        return b;
    }

    private static Block[] getAllTileBlocks() {
        return TILE_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new);
    }

    public static final RegistryObject<SoundEvent> CLU_DOOR_SOUND = makeSoundEvent("block.clu_door");
    public static final RegistryObject<SoundEvent> WHITE_DOOR_OPEN_SOUNDS = makeSoundEvent("block.white_door.open");
    public static final RegistryObject<SoundEvent> HEALING_CHAMBER_SOUND = makeSoundEvent("block.healing_chamber");
    public static final RegistryObject<SoundEvent> BOOKSHELF_SOUND = makeSoundEvent("block.bookshelf");
    public static final RegistryObject<SoundEvent> PORTAL_SOUND = makeSoundEvent("block.portal_pad");


    public static final RegistryObject<Block> CLU_DOOR = regWithItem("clu_door", () ->
            new DoorBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_WHITE)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(1.5F),
                    Locations.CLU_DOOR,CLU_DOOR_SOUND,
                    86,64,16,0.0245f));

    public static final RegistryObject<BlockEntityType<DoorBlockTile>> CLU_DOOR_TILE = TILES
            .register("clu_door", () -> BlockEntityType.Builder.of(DoorBlockTile::new,
                    ModRegistry.CLU_DOOR.get(),ModRegistry.WHITE_DOOR.get()).build(null));

    public static final RegistryObject<Block> HEALING_CHAMBER = regWithItem("healing_chamber", () ->
            new HealingChamberBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_WHITE)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(1.5F)));

    public static final RegistryObject<BlockEntityType<HealingChamberTile>> HEALING_CHAMBER_TILE = TILES
            .register("healing_chamber", () -> BlockEntityType.Builder.of(HealingChamberTile::new,
                    ModRegistry.HEALING_CHAMBER.get()).build(null));


    public static final RegistryObject<Block> BLACK_CHAIR = regTileBlock("black_chair", () ->
            new ChairBlock(BlockBehaviour.Properties.of(Material.GLASS, MaterialColor.COLOR_BLACK)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .strength(1), Locations.BLACK_CHAIR, 32, 24, 13));

    public static final RegistryObject<Block> KITCHEN_CHAIR = regTileBlock("kitchen_chair", () ->
            new ChairBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1), Locations.KITCHEN_CHAIR, 32, 16, 12f));

    public static final RegistryObject<Block> CLU_INTERFACE = regTileBlock("clu_interface", () ->
            new ChairBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1), Locations.CLU_INTERFACE, 16, 48, 12f));

    public static final RegistryObject<Block> THRONE = regTileBlock("clu_throne", () ->
            new ChairBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1), Locations.CLU_THRONE, 24, 105, 42, 13));

    public static final RegistryObject<Block> BLACK_COUCH = regTileBlock("black_couch", () ->
            new ChairBlock(BlockBehaviour.Properties.copy(BLACK_CHAIR.get()), Locations.BLACK_COUCH, 32, 34, 20, 13, 2));

    public static final RegistryObject<Block> WHITE_CHAIR = regTileBlock("white_chair", () ->
            new TronBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1), Locations.WHITE_CHAIR, 16, 16));

    public static final RegistryObject<Block> RECLINER = regTileBlock("recliner", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.RECLINER, 11, 18, 32));

    public static final RegistryObject<Block> BEDSIDE = regTileBlock("bedside", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.BEDSIDE, 11, 24, 14));

    public static final RegistryObject<Block> KITCHEN_TABLE = regTileBlock("kitchen_table", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()).sound(SoundType.GLASS), Locations.KITCHEN_TABLE, 21.5f, 48, 16));

    public static final RegistryObject<Block> METAL_TABLE = regTileBlock("metal_table", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.METAL_TABLE, 16, 16, 16));

    public static final RegistryObject<Block> QUORRAS_DRESSER = regTileBlock("quorras_dresser", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.QUORRAS_DRESSER, 32, 48, 16));

    public static final RegistryObject<Block> SIMPLE_GLASS_TABLE = regTileBlock("simple_glass_table", () ->
            new TronBlock(BlockBehaviour.Properties.copy(KITCHEN_TABLE.get()), Locations.SIMPLE_GLASS_TABLE, 16, 16, 16));

    public static final RegistryObject<Block> WHITE_TABLE = regTileBlock("white_table", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.WHITE_TABLE, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF = regTileBlock("bookshelf", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.BOOKSHELF, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF2 = regTileBlock("bookshelf2", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.BOOKSHELF2, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF3 = regTileBlock("bookshelf3", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.BOOKSHELF3, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF_END = regTileBlock("bookshelf_end", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.BOOKSHELF_END, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF_END2 = regTileBlock("bookshelf_end2", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.BOOKSHELF_END2, 16, 16, 16));

    public static final RegistryObject<Block> FLYNN_LAMP = regTileBlock("flynn_lamp", () ->
            new LampBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()).lightLevel(s -> s.getValue(LampBlock.LIT) ? 15 : 0),
                    Locations.FLYNN_LAMP, Locations.FLYNN_LAMP_OFF, 16, 16, 16));

    public static final RegistryObject<Block> CLU_BACK_DOOR = regTileBlock("clu_back_door", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.CLU_BACK_DOOR, 120, 32, 120));

    public static final RegistryObject<Block> CLU_BACK_STAIRS = regTileBlock("clu_back_stairs", () ->
            new CluBlackStairsBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.CLU_BACK_STAIRS));

    public static final RegistryObject<Block> FIREPLACE = regTileBlock("fireplace", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()).lightLevel(s -> 15), Locations.FIREPLACE, 32, 58, 32));

    public static final RegistryObject<Block> CHAND_ON = regTileBlock("chand_on", () ->
            new ChandelierBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()).lightLevel(s -> s.getValue(LampBlock.LIT) ? 15 : 0),
                    Locations.CHAND_ON, Locations.CHAND, 32, 32, 32));

    public static final RegistryObject<Block> CLU_GLOW_WALL = regTileBlock("clu_glow_wall", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()).lightLevel(s -> 15), Locations.CLU_GLOW_WALL, 16, 16, 16));

    public static final RegistryObject<Block> SERVER = regTileBlock("server", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER, 16, 16, 16));

    public static final RegistryObject<Block> SERVER_BLANK = regTileBlock("server_blank", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER_BLANK, 16, 16, 16));

    public static final RegistryObject<Block> SERVER_BLANK_MIDDLE = regTileBlock("server_blank_middle", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER_BLANK_MIDDLE, 16, 16, 16));

    public static final RegistryObject<Block> SERVER_BLANK_TOP = regTileBlock("server_blank_top", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER_BLANK_TOP, 16, 16, 16));

    public static final RegistryObject<Block> SERVER_MIDDLE = regTileBlock("server_middle", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER_MIDDLE, 16, 16, 16));

    public static final RegistryObject<Block> SERVER_RED = regTileBlock("server_red", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER_RED, 16, 16, 16));

    public static final RegistryObject<Block> SERVER_RED_MIDDLE = regTileBlock("server_red_middle", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER_RED_MIDDLE, 16, 16, 16));

    public static final RegistryObject<Block> SERVER_RED_TOP = regTileBlock("server_red_top", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER_RED_TOP, 16, 16, 16));

    public static final RegistryObject<Block> SERVER_TOP = regTileBlock("server_top", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.SERVER_TOP, 16, 16, 16));

    public static final RegistryObject<Block> CLU_LITTLE_WINDOW = regTileBlock("clu_little_window", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.CLU_LITTLE_WINDOW, 49.5f, 28, 69));

    public static final RegistryObject<Block> CLU_LITTLE_WINDOW_UPSIDEDOWN = regTileBlock("clu_little_window_upsidedown", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.CLU_LITTLE_WINDOW_UPSIDEDOWN, 49.5f, 28, 69));

    public static final RegistryObject<Block> PORTAL_PAD = regTileBlock("portal_pad", () ->
            new PortalPadBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.PORTAL_PAD, 0.1f, 120, 120));

    public static final RegistryObject<Block> FLYNN = regTileBlock("flynn", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.FLYNN, 16, 48, 16));

    public static final RegistryObject<Block> FLYNN_SIGN = regTileBlock("flynn_sign", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()).sound(SoundType.GLASS)
                    .emissiveRendering((a, b, c) -> true), Locations.FLYNN_SIGN, 48, 48, 17));

    public static final RegistryObject<Block> FLYNN_SIGN2 = regTileBlock("flynn_sign2", () ->
            new TronBlock(BlockBehaviour.Properties.copy(FLYNN_SIGN.get()), Locations.FLYNN_SIGN2, 48, 48, 17));

    public static final RegistryObject<Block> INFINITAS = regTileBlock("infinitas", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.INFINITAS, 64, 64, 1));

    public static final RegistryObject<Block> TRONLIVES = regTileBlock("tronlives", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.TRONLIVES, 64, 64, 1));

    public static final RegistryObject<Block> CURTAIN = regTileBlock("curtain", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.CURTAIN, 64, 96, 1));

    public static final RegistryObject<Block> CURTAIN2 = regTileBlock("curtain2", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.CURTAIN2, 64, 96, 1));

    public static final RegistryObject<Block> WORLD = regTileBlock("world", () ->
            new GlobeBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.WORLD, 32, 32, 32));

    public static final RegistryObject<Block> CLU_WORLD = regTileBlock("clu_world", () ->
            new GlobeBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.CLU_WORLD, 32, 32, 32));

    public static final RegistryObject<Block> PORTAL_BRIDGE = regTileBlock("portal_bridge", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.PORTAL_BRIDGE, 0.1f, 24, 100));

    public static final RegistryObject<Block> PORTAL_BRIDGE2 = regTileBlock("portal_bridge2", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.PORTAL_BRIDGE2, 0.1f, 24, 100));

    public static final RegistryObject<Block> PORTAL_BRIDGE3 = regTileBlock("portal_bridge3", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.PORTAL_BRIDGE3, 0.1f, 24, 100));

    public static final RegistryObject<Block> PORTAL_BRIDGE4 = regTileBlock("portal_bridge4", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.PORTAL_BRIDGE4, 0.1f, 24, 100));

    public static final RegistryObject<Block> BLACK_BED = regTileBlock("black_bed", () ->
            new BigBedBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.BLACK_BED, 32, 48, 48));

    public static final RegistryObject<Block> FLYNNS_BED = regTileBlock("flynns_bed", () ->
            new BigBedBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()), Locations.FLYNNS_BED, 32, 48, 48));


    public static final RegistryObject<Block> WHITE_DOOR = regTileBlock("white_door", () ->
            new DoorBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),
                    Locations.WHITE_DOOR_FRAME,WHITE_DOOR_OPEN_SOUNDS,
                    58, 70, 4, 2f));


    public static final RegistryObject<Block> CLU_STAIRS = regWithItem("clu_stairs", () ->
            new CluStairsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(1.5F)));


    public static final RegistryObject<BlockEntityType<GlobeBlockTile>> GLOBE_TILE = TILES
            .register("globe", () -> BlockEntityType.Builder.of(GlobeBlockTile::new,
                    CLU_WORLD.get(), WORLD.get()).build(null));

    public static final RegistryObject<BlockEntityType<PortalPadBlockTile>> PORTAL_PAD_TILE = TILES
            .register("portal_pad", () -> BlockEntityType.Builder.of(PortalPadBlockTile::new,
                    PORTAL_PAD.get()).build(null));

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

    public static final RegistryObject<EntityType<CluStepEntity>> CLU_STEP_ENTITY = ENTITIES.register("clu_step",
            () -> EntityType.Builder.<CluStepEntity>of(CluStepEntity::new, MobCategory.MISC)
                    .sized(0.75f, 0.25f)
                    .clientTrackingRange(8)
                    .build("clu_step"));

    public static final RegistryObject<EntityType<ScreenEntity>> SCREEN = ENTITIES.register("screen",
            () -> EntityType.Builder.<ScreenEntity>of(ScreenEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("screen"));
    public static final RegistryObject<Item> SCREEN_ITEM = regItem("screen",
            () -> new ScreenItem(new Item.Properties().tab(MOD_TAB)));

}
