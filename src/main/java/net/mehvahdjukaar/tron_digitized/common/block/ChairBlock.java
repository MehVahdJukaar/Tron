package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.common.entity.ChairEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class ChairBlock extends TronBlock {

    protected final VoxelShape shapeBaseX;
    protected final VoxelShape shapeBaseZ;
    public static final BooleanProperty SAT_IN = BooleanProperty.create("sat_in");

    private final float chairHeight;
    private final int maxPassengers;

    public ChairBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float chairHeight) {
        this(properties, modelLoc, height, width, width, chairHeight);
    }

    public ChairBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float length, float chairHeight) {
        this(properties, modelLoc, height, width, length, chairHeight, 1);
    }

    public ChairBlock(Properties properties, ResourceLocation modelLoc, float height, float width, float length, float chairHeight, int seats) {
        super(properties, modelLoc, height, width, length);
        this.chairHeight = (chairHeight - 13) / 16f;
        float w = width / 2f;
        float l = length / 2f;
        shapeBaseX = Block.box(8 - w, 0, 8 - l, 8 + w, chairHeight, 8 + l);
        shapeBaseZ = Block.box(8 - l, 0, 8 - w, 8 + l, chairHeight, 8 + w);

        this.registerDefaultState(this.getStateDefinition().any().setValue(SAT_IN, false).setValue(FACING, Direction.NORTH));
        this.maxPassengers = seats;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        this.fixState(worldIn, pos, state);
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockState(pos.above()).isAir() && player.getVehicle() == null) {

            if (!worldIn.isClientSide) {
                ChairEntity entity;
                var chairs = worldIn.getEntitiesOfClass(ChairEntity.class, new AABB(pos));
                if (chairs.isEmpty()) {
                    entity = new ChairEntity(worldIn, this.getChairHeight(), this.maxPassengers);
                    entity.setPos((double) pos.getX() + 0.5D, (double) pos.getY() + 0.6D, (double) pos.getZ() + 0.5D);
                    worldIn.addFreshEntity(entity);
                    if (!state.getValue(SAT_IN)) {
                        worldIn.setBlockAndUpdate(pos, state.setValue(SAT_IN, true));
                    }
                } else {
                    entity = chairs.get(0);
                }
                //var aa = worldIn.getEntitiesOfClass(Pig.class, new AABB(pos));
                //aa.forEach(e->e.startRiding(entity));
                if (entity.getPassengers().size() < this.maxPassengers) {
                    player.startRiding(entity);
                    return InteractionResult.CONSUME;
                }
            } else return InteractionResult.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    public float getChairHeight() {
        return chairHeight;
    }

    @Override
    protected VoxelShape getColliderShape(BlockState pState) {
        return (pState.getValue(FACING).getAxis() == Direction.Axis.X) ? shapeBaseX : shapeBaseZ;
    }

    private void fixState(Level worldIn, BlockPos pos, BlockState state) {
        BlockState target = this.getStateFor(worldIn, pos);
        if (!target.equals(state)) {
            worldIn.setBlockAndUpdate(pos, target);
        }
    }

    private BlockState getStateFor(Level world, BlockPos pos) {
        return this.defaultBlockState().setValue(SAT_IN,
                world.getEntitiesOfClass(ChairEntity.class, (new AABB(pos, pos.above())).inflate(0.4D), (e) -> e.blockPosition().equals(pos)).size() > 0);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SAT_IN);
    }

}

