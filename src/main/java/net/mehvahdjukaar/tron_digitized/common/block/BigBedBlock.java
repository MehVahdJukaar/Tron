package net.mehvahdjukaar.tron_digitized.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.Function;

public class BigBedBlock extends BedBlock implements EntityBlock, ICustomModelProvider, ICustomBed {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected final VoxelShape renderShapeX;
    protected final VoxelShape renderShapeZ;
    protected final VoxelShape shapeX;
    protected final VoxelShape shapeZ;
    protected final ResourceLocation modelLocation;
    private final float bedHeight;

    public BigBedBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float length) {
        super(DyeColor.WHITE, properties);
        this.modelLocation = modelLoc;
        float w = width / 2f;
        float l = length / 2f;
        shapeZ = Block.box(8 - w, 0, 8 - l, 8 + w, height, 8 + l);
        shapeX = Block.box(8 - l, 0, 8 - w, 8 + l, height, 8 + w);
        //bigger collision shapes
        w++;
        l++;
        renderShapeX = Block.box(8 - w, 0, 8 - l, 8 + w, height + 1, 8 + l);
        renderShapeZ = Block.box(8 - l, 0, 8 - w, 8 + l, height + 1, 8 + w);

        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.HEAD).setValue(FACING, Direction.NORTH)
                .setValue(OCCUPIED, false));

        this.bedHeight = height / 16f;
    }

    @Override
    public Vec3 getBedOffset(BlockState state) {
        var v = state.getValue(FACING).step();
        double move = 0;
        return new Vec3(move*v.x(), bedHeight + 0.25f, move*v.z());
    }


    @Deprecated
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return pState;
    }

    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        this.spawnDestroyParticles(pLevel, pPlayer, pPos, pState);
        if (pState.is(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinAi.angerNearbyPiglins(pPlayer, false);
        }

        pLevel.gameEvent(pPlayer, GameEvent.BLOCK_DESTROY, pPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @org.jetbrains.annotations.Nullable LivingEntity pPlacer, ItemStack pStack) {
    }

    public VoxelShape getBaseShape(BlockState state) {
        return (state.getValue(FACING).getAxis() == Direction.Axis.X) ? shapeX : shapeZ;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getBaseShape(pState);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return getBaseShape(pState);
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getBaseShape(pState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pContext instanceof EntityCollisionContext e && e.getEntity() != null) {
            return getColliderShape(pState);
        }
        return (pState.getValue(FACING).getAxis() == Direction.Axis.X) ? renderShapeX : renderShapeZ;
    }

    protected VoxelShape getColliderShape(BlockState pState) {
        return getBaseShape(pState);
    }

    @Override
    public ResourceLocation getCustomModelLocation(BlockState state) {
        return modelLocation;
    }


    @Override
    public Function<BlockState, Float> getYawGetter() {
        return s -> s.getValue(FACING).toYRot();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TronBlockTile(pPos, pState);
    }

}
