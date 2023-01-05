package net.mehvahdjukaar.tron_digitized.common.items;

import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;


public class DiscItem extends Item {

    public DiscItem(Properties pProperties) {

        super(pProperties);

        ItemProperties.register(ModRegistry.DISC.get(), new ResourceLocation("disc_editing_start"), (ItemStack, ClientLevel, LivingEntity, Int) -> {
            return LivingEntity != null && LivingEntity.isUsingItem() && LivingEntity.getUseItem() == ItemStack ? 1.0F : 0.0F;
        });

        ItemProperties.register(ModRegistry.DISC.get(), new ResourceLocation("disc_editing_end"), (ItemStack, ClientLevel, LivingEntity, Int) -> {
            return LivingEntity != null && LivingEntity.isUsingItem() && LivingEntity.getUseItem() == ItemStack ? 1.0F : 0.0F;
        });

    }



}
