package net.mehvahdjukaar.tron_digitized.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TronBlock extends BaseEntityBlock implements ICustomModelProvider {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected final VoxelShape renderShapeX;
    protected final VoxelShape renderShapeZ;
    protected final VoxelShape shapeX;
    protected final VoxelShape shapeZ;
    protected final ResourceLocation modelLocation;

    public TronBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float length) {
        super(properties);
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

    }

    public TronBlock(Properties properties, ResourceLocation modelLoc, int height, int width) {
        this(properties, modelLoc, height, width, width);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
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
