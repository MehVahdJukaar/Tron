package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class PortalPadBlock extends TronBlock{

    private static final int SOUND_DURATION = 32*20;

    public PortalPadBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float length) {
        super(properties, modelLoc, height, width, length);
    }

    public PortalPadBlock(Properties properties, ResourceLocation modelLoc, int height, int width) {
        super(properties, modelLoc, height, width);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
        if (pRand.nextInt(100) == 0) {
            pLevel.playLocalSound((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D,
                    ModRegistry.PORTAL_SOUND.get(), SoundSource.BLOCKS, 0.5F, pRand.nextFloat() * 0.4F + 0.8F, false);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        pLevel.scheduleTick(pPos, this, SOUND_DURATION);
        pLevel.playSound(null, pPos, ModRegistry.PORTAL_SOUND.get(),
                SoundSource.BLOCKS, 0.5F, pRandom.nextFloat() * 0.4F + 0.8F);

    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        pLevel.scheduleTick(pPos, this, 20);
    }
}
