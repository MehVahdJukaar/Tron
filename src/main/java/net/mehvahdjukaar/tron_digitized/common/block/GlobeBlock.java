package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.Tron;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GlobeBlock extends TronBlock {

    public GlobeBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float length) {
        super(properties, modelLoc, height, width, length);
    }

    public GlobeBlock(Properties properties, ResourceLocation modelLoc, int height, int width) {
        super(properties, modelLoc, height, width);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GlobeBlockTile(pPos,pState);
    }
}
