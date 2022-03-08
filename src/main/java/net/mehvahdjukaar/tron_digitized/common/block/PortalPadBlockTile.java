package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PortalPadBlockTile extends TronBlockTile {

    private int counter = 0;

    private static final int SOUND_DURATION = 33*20;

    public PortalPadBlockTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModRegistry.PORTAL_PAD_TILE.get(), pWorldPosition, pBlockState);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, PortalPadBlockTile tile) {
        if (level.isClientSide && tile.counter++%SOUND_DURATION==0) {
            level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                    ModRegistry.PORTAL_SOUND.get(), SoundSource.BLOCKS, 1, 1, false);
        }
    }
}
