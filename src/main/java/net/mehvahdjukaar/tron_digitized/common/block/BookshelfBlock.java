package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.client.gui.BookshelfGui;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BookshelfBlock extends TronBlock{

    public BookshelfBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float length) {
        super(properties, modelLoc, height, width, length);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide){
            BookshelfGui.open(pPos);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }
}
