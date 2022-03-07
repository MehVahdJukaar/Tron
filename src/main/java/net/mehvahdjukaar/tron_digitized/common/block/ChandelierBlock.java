package net.mehvahdjukaar.tron_digitized.common.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChandelierBlock extends LampBlock{

    protected final VoxelShape shape;

    public ChandelierBlock(Properties properties, ResourceLocation modelLoc, ResourceLocation offLoc, float height, float width, float length) {
        super(properties, modelLoc, offLoc, height, width, length);
        float w = width / 2f;
        float l = length / 2f;
        shape = Block.box(8 - w, 0-height, 8 - l, 8 + w, 16, 8 + l);
    }

    @Override
    public VoxelShape getBaseShape(BlockState state) {
        return shape;
    }
}
