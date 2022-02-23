package net.mehvahdjukaar.tron_digitized.common.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public interface ICustomModelProvider {

    ResourceLocation getCustomModelLocation();

    default boolean isTranslucent(){
        return true;
    };

    default Function<BlockState, Float> getYawGetter(){
        return s->0f;
    }

}
