package net.mehvahdjukaar.tron_digitized.init;

import net.mehvahdjukaar.tron_digitized.Tron;
import net.mehvahdjukaar.tron_digitized.common.block.*;
import net.mehvahdjukaar.tron_digitized.common.entity.ChairEntity;
import net.mehvahdjukaar.tron_digitized.common.entity.CluStepEntity;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
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
                    .strength(1), ClientSetup.KITCHEN_CHAIR, 32, 16, 12f));

    public static final RegistryObject<Block> THRONE = regTileBlock("clu_throne", () ->
            new ChairBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1),ClientSetup.CLU_THRONE,24, 105,42,13));

    public static final RegistryObject<Block> BLACK_COUCH = regTileBlock("black_couch", () ->
            new ChairBlock(BlockBehaviour.Properties.copy(BLACK_CHAIR.get()),ClientSetup.BLACK_COUCH,32,34,20,13, 2));

    public static final RegistryObject<Block> WHITE_CHAIR = regTileBlock("white_chair", () ->
            new TronBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.QUARTZ)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .strength(1),ClientSetup.WHITE_CHAIR, 16, 16));

    public static final RegistryObject<Block> RECLINER = regTileBlock("recliner", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.RECLINER, 11, 18, 32));

    public static final RegistryObject<Block> BEDSIDE = regTileBlock("bedside", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.BEDSIDE, 11, 24, 14));

    public static final RegistryObject<Block> KITCHEN_TABLE = regTileBlock("kitchen_table", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()).sound(SoundType.GLASS),ClientSetup.KITCHEN_TABLE, 21.5f, 48, 16));

    public static final RegistryObject<Block> METAL_TABLE = regTileBlock("metal_table", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.METAL_TABLE, 16, 16, 16));

    public static final RegistryObject<Block> QUORRAS_DRESSER = regTileBlock("quorras_dresser", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.QUORRAS_DRESSER, 32, 48, 16));

    public static final RegistryObject<Block> SIMPLE_GLASS_TABLE = regTileBlock("simple_glass_table", () ->
            new TronBlock(BlockBehaviour.Properties.copy(KITCHEN_TABLE.get()),ClientSetup.SIMPLE_GLASS_TABLE, 16, 16, 16));

    public static final RegistryObject<Block> WHITE_TABLE = regTileBlock("white_table", () ->
            new TronBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.WHITE_TABLE, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF = regTileBlock("bookshelf", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.BOOKSHELF, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF2 = regTileBlock("bookshelf2", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.BOOKSHELF2, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF3 = regTileBlock("bookshelf3", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.BOOKSHELF3, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF_END = regTileBlock("bookshelf_end", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.BOOKSHELF_END, 16, 16, 16));

    public static final RegistryObject<Block> BOOKSHELF_END2 = regTileBlock("bookshelf_end2", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.BOOKSHELF_END2, 16, 16, 16));

    public static final RegistryObject<Block> FLYNN_LAMP = regTileBlock("flynn_lamp", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.FLYNN_LAMP, 16, 16, 16));

    public static final RegistryObject<Block> CLU_BACK_DOOR = regTileBlock("clu_back_door", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.CLU_BACK_DOOR, 160, 32, 160));

    public static final RegistryObject<Block> CLU_BACK_STAIRS = regTileBlock("clu_back_stairs", () ->
            new BookshelfBlock(BlockBehaviour.Properties.copy(WHITE_CHAIR.get()),ClientSetup.CLU_BACK_STAIRS, 46.5f, 32, 90.5f));




    public static final RegistryObject<Block> CLU_STAIRS = regWithItem("clu_stairs", () ->
            new CluStairsBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(1.5F)));

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


}
