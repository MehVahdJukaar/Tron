package net.mehvahdjukaar.tron_digitized.init;

import net.mehvahdjukaar.tron_digitized.Tron;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class Locations {

    public static final Set<ResourceLocation> CUSTOM_MODELS = new HashSet<>();



    public static ResourceLocation addCustomModel(String name){
        var res =  new ResourceLocation(Tron.MOD_ID + ":block/"+name);
        CUSTOM_MODELS.add(res);
        return res;
    }

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
    public static final ResourceLocation FIREPLACE = addCustomModel("fireplace");
    public static final ResourceLocation CHAND_ON = addCustomModel("chand_on");
    public static final ResourceLocation CHAND = addCustomModel("chand");
    public static final ResourceLocation CLU_GLOW_WALL = addCustomModel("clu_glow_wall");
    public static final ResourceLocation CLU_INTERFACE = addCustomModel("clu_interface");
    public static final ResourceLocation SERVER = addCustomModel("server");
    public static final ResourceLocation SERVER_BLANK = addCustomModel("server_blank");
    public static final ResourceLocation SERVER_BLANK_MIDDLE = addCustomModel("server_blank_middle");
    public static final ResourceLocation SERVER_BLANK_TOP = addCustomModel("server_blank_top");
    public static final ResourceLocation SERVER_MIDDLE = addCustomModel("server_middle");
    public static final ResourceLocation SERVER_RED = addCustomModel("server_red");
    public static final ResourceLocation SERVER_RED_MIDDLE = addCustomModel("server_red_middle");
    public static final ResourceLocation SERVER_RED_TOP = addCustomModel("server_red_top");
    public static final ResourceLocation SERVER_TOP = addCustomModel("server_top");
    public static final ResourceLocation CLU_LITTLE_WINDOW = addCustomModel("clu_little_window");
    public static final ResourceLocation CLU_LITTLE_WINDOW_UPSIDEDOWN = addCustomModel("clu_little_window_upsidedown");
    public static final ResourceLocation PORTAL_PAD = addCustomModel("portal_pad");
    public static final ResourceLocation FLYNN = addCustomModel("flynn");
    public static final ResourceLocation FLYNN_SIGN = addCustomModel("flynn_sign");
    public static final ResourceLocation FLYNN_SIGN2 = addCustomModel("flynn_sign2");
    public static final ResourceLocation INFINITAS = addCustomModel("infinitas");
    public static final ResourceLocation TRONLIVES = addCustomModel("tronlives");
    public static final ResourceLocation CURTAIN = addCustomModel("curtain");
    public static final ResourceLocation CURTAIN2 = addCustomModel("curtain2");
    public static final ResourceLocation SCREEN = addCustomModel("screen");
    public static final ResourceLocation WORLD = addCustomModel("world");
    public static final ResourceLocation SCREEN2 = addCustomModel("screen2");
    public static final ResourceLocation CLU_WORLD = addCustomModel("clu_world");
    public static final ResourceLocation SCREEN3 = addCustomModel("screen3");
    public static final ResourceLocation SCREEN4 = addCustomModel("screen4");
    public static final ResourceLocation PORTAL_BRIDGE = addCustomModel("portal_bridge");
    public static final ResourceLocation PORTAL_BRIDGE2 = addCustomModel("portal_bridge2");
    public static final ResourceLocation PORTAL_BRIDGE3 = addCustomModel("portal_bridge3");
    public static final ResourceLocation PORTAL_BRIDGE4 = addCustomModel("portal_bridge4");
    public static final ResourceLocation BLACK_BED = addCustomModel("black_bed");
    public static final ResourceLocation FLYNNS_BED = addCustomModel("flynns_bed");
    public static final ResourceLocation WHITE_DOOR = addCustomModel("white_door");
    public static final ResourceLocation WHITE_DOOR_FRAME = addCustomModel("white_door_frame");

}
