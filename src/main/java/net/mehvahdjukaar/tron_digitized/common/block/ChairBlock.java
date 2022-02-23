package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.common.entity.ChairEntity;
import net.mehvahdjukaar.tron_digitized.init.ClientSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.Function;

public class ChairBlock extends TronBlock {

    protected static final VoxelShape SHAPE_BASE = Block.box(-4, 0, -4, 20, 13, 20);
    public static final BooleanProperty SAT_IN = BooleanProperty.create("sat_in");

    private final float chairHeight;

    public ChairBlock(Properties properties, ResourceLocation modelLoc, float chairHeight) {
        this(properties,32,24, modelLoc, chairHeight);
    }

    public ChairBlock(Properties properties, int height, int width, ResourceLocation modelLoc, float chairHeight) {
        super(properties,modelLoc,height,width);
        this.chairHeight = chairHeight;
        this.registerDefaultState(this.getStateDefinition().any().setValue(SAT_IN, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        this.fixState(worldIn, pos, state);
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (! state.getValue(SAT_IN) && worldIn.getBlockState(pos.above()).isAir() && player.getVehicle() == null) {
            if (!worldIn.isClientSide) {
                ChairEntity entity = new ChairEntity(worldIn, this.getChairHeight());
                entity.setPos((double) pos.getX() + 0.5D, (double) pos.getY() + 0.6D, (double) pos.getZ() + 0.5D);
                worldIn.addFreshEntity(entity);
                player.startRiding(entity);
                worldIn.setBlockAndUpdate(pos, state.setValue(SAT_IN, true));
            }

            return InteractionResult.SUCCESS;
        } else {
            return super.use(state, worldIn, pos, player, handIn, hit);
        }
    }

    public float getChairHeight(){
        return chairHeight;
    };

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BASE;
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

    @Override
    public ResourceLocation getCustomModelLocation() {
        return ClientSetup.BLACK_CHAIR;
    }

}

