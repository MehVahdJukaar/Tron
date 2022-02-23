package net.mehvahdjukaar.tron_digitized.common.block;

import net.mehvahdjukaar.tron_digitized.init.ClientSetup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class KitchenChair extends ChairBlock{

    protected static final VoxelShape SHAPE_BASE = Block.box(-5, 0, -5, 19, 16, 19);

    public KitchenChair(Properties properties) {
        super(properties,36, 18, ClientSetup.KITCHEN_CHAIR, 0.3f);
    }

    @Override
    public ResourceLocation getCustomModelLocation() {
        return ClientSetup.KITCHEN_CHAIR;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BASE;
    }

    @Override
    public boolean isTranslucent() {
        return false;
    }

    @Override
    public float getChairHeight() {
        return 0.2f;
    }
}
