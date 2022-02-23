package net.mehvahdjukaar.tron_digitized.common.block;

import com.google.common.base.Equivalence;
import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TronBlockTIle extends BlockEntity {

    @Nullable
    private final ResourceLocation modelLocation;
    private final boolean translucent;
    private final Function<BlockState, Float> yawGetter;

    public TronBlockTIle(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModRegistry.CUSTOM_BLOCK_TILE.get(), pWorldPosition, pBlockState);
        if(pBlockState.getBlock() instanceof ICustomModelProvider modelProvider){
            this.modelLocation = modelProvider.getCustomModelLocation();
            this.translucent = modelProvider.isTranslucent();
            this.yawGetter = modelProvider.getYawGetter();
        }
        else {
            this.modelLocation = null;
            this.translucent = true;
            this.yawGetter = s->0f;
        }
    }

    @Nullable
    public ResourceLocation getModelLocation() {
        return modelLocation;
    }

    public boolean isTranslucent() {
        return this.translucent;
    }

    public float getYaw() {
        return this.yawGetter.apply(this.getBlockState());
    }
}
