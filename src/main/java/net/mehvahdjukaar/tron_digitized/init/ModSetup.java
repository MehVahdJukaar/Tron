package net.mehvahdjukaar.tron_digitized.init;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

            //NetworkHandler.registerMessages();


        });
    }
}
