package net.mehvahdjukaar.tron_digitized.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class LampBlock extends TronBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private final ResourceLocation offLoc;

    public LampBlock(Properties properties, ResourceLocation modelLoc,  ResourceLocation offLoc,
                     float height, float width, float length) {
        super(properties, modelLoc, height, width, length);
        this.offLoc = offLoc;
    }

    public LampBlock(Properties properties, ResourceLocation modelLoc,ResourceLocation offLoc,
                     int height, int width) {
        super(properties, modelLoc, height, width);
        this.offLoc = offLoc;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(LIT);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockState blockstate = this.toggle(pState, pLevel, pPos);
            float f = blockstate.getValue(LIT) ? 0.6F : 0.5F;
            pLevel.playSound(null, pPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
            pLevel.gameEvent(pPlayer, blockstate.getValue(LIT) ? GameEvent.BLOCK_SWITCH : GameEvent.BLOCK_UNSWITCH, pPos);
            return InteractionResult.CONSUME;
        }
    }

    public BlockState toggle(BlockState pState, Level pLevel, BlockPos pPos) {
        pState = pState.cycle(LIT);
        pLevel.setBlock(pPos, pState, 3);
        return pState;
    }

    @Override
    public ResourceLocation getCustomModelLocation(BlockState state) {
        return state.getValue(LIT) ? super.getCustomModelLocation(state) : offLoc;
    }
}
