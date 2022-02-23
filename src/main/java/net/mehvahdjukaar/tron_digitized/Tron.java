package net.mehvahdjukaar.tron_digitized;


import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.mehvahdjukaar.tron_digitized.init.ModSetup;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author: MehVahdJukaar
 */
@Mod(Tron.MOD_ID)
public class Tron {
    public static final String MOD_ID = "tron_digitized";

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    private static final Logger LOGGER = LogManager.getLogger();

    public Tron() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ModSetup::init);

        MinecraftForge.EVENT_BUS.register(this);

        ModRegistry.init(bus);
    }

}
