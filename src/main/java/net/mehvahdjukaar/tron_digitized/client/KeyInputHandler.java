package net.mehvahdjukaar.tron_digitized.client;

import net.mehvahdjukaar.tron_digitized.client.gui.DiscEditingGui;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.InputEvent;

public class KeyInputHandler {

    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyMappings.discEditingKeyMapping.consumeClick() && Minecraft.getInstance().player.getMainHandItem().is(ModRegistry.DISC.get())) {
            Minecraft.getInstance().player.sendMessage(new TextComponent("Hello World!"), null);
            DiscEditingGui.open(event);
        }
    }
}
