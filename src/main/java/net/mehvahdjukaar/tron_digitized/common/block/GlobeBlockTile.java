package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GlobeBlockTile extends TronBlockTile{

    public GlobeBlockTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModRegistry.GLOBE_TILE.get(), pWorldPosition, pBlockState);
    }
}
