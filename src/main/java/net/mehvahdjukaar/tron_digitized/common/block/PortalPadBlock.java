package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PortalPadBlock extends TronBlock implements EntityBlock {

    public PortalPadBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float length) {
        super(properties, modelLoc, height, width, length);
    }

    public PortalPadBlock(Properties properties, ResourceLocation modelLoc, int height, int width) {
        super(properties, modelLoc, height, width);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PortalPadBlockTile(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return HealingChamberBlock.getTicker(pBlockEntityType, ModRegistry.PORTAL_PAD_TILE.get(), PortalPadBlockTile::tick);
    }
}
