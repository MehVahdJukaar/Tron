package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DoorBlockTile extends TronBlockTile {

    private final float speed;
    public float openingProgress = 0;
    public float prevOpeningProgress = 0;

    public DoorBlockTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(pWorldPosition, pBlockState);
        if(pBlockState.getBlock() instanceof DoorBlock doorBlock){
            speed = doorBlock.openingSpeed;
        }else speed = 0.05f;
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModRegistry.CLU_DOOR_TILE.get();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putFloat("openingProgress", openingProgress);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.openingProgress = pTag.getFloat("openingProgress");
        this.prevOpeningProgress = this.openingProgress;
    }


    public static void tick(Level level, BlockPos pos, BlockState state, DoorBlockTile tile) {
        boolean open = state.getValue(DoorBlock.OPEN);
        tile.prevOpeningProgress = tile.openingProgress;
        if(open){
            if(tile.openingProgress<1)
            tile.openingProgress = Math.min(tile.openingProgress+tile.speed, 1);
        }
        else{
            if(tile.openingProgress>0)
            tile.openingProgress = Math.max(tile.openingProgress-tile.speed, 0);
        }
    }
}
