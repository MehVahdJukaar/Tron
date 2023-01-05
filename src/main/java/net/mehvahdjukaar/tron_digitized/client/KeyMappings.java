package net.mehvahdjukaar.tron_digitized.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyMappings {

    public static final String KEY_CATEGORIES_TRON = "key.categories.tron";
    public static final String KEY_DISC_EDITING = "key.discEditing";

    public static KeyMapping discEditingKeyMapping;

    public static void init() {
        discEditingKeyMapping = new KeyMapping(KEY_DISC_EDITING, KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.right.shift"), KEY_CATEGORIES_TRON);
        ClientRegistry.registerKeyBinding(discEditingKeyMapping);
    }

}
