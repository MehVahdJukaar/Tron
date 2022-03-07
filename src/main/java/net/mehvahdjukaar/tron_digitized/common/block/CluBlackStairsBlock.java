package net.mehvahdjukaar.tron_digitized.common.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CluBlackStairsBlock extends TronBlock {

    private final VoxelShape shapeNorth;
    private final VoxelShape shapeSouth;
    private final VoxelShape shapeEast;
    private final VoxelShape shapeWest;

    public CluBlackStairsBlock(Properties properties, ResourceLocation modelLoc) {
        super(properties, modelLoc, 3.5f, 80, 48);
        float h = 3.5f;
        float w2 = 75 / 2f;
        float l2 = 42.5f;

        float w3 = 65/2f;
        float l3 = 37f;

        float w4 = 54/2f;
        float l4 = 32f;

        shapeNorth = Shapes.or(this.shapeZ,
                Block.box(8 - w2, h, 8 - 48/2f,
                        8 + w2, 2*h, 8 - 48/2f + l2),
                Block.box(8 - w3, 2*h, 8 - 48/2f,
                        8 + w3, 3*h, 8 - 48/2f +l3),
                Block.box(8 - w4, 3*h, 8 - 48/2f,
                        8 + w4, 4.5f*h, 8 - 48/2f +l4));


        shapeSouth =  Shapes.or(this.shapeZ,
                Block.box(8 - w2, h, 8 + 48/2f - l2,
                        8 + w2, 2*h, 8 + 48/2f),
                Block.box(8 - w3, 2*h, 8 + 48/2f - l3,
                        8 + w3, 3*h, 8 + 48/2f),
                Block.box(8 - w4, 3*h, 8 + 48/2f - l4,
                        8 + w4, 4.5f*h, 8 + 48/2f));


        shapeEast = Shapes.or(this.shapeX,
                Block.box(8 + 48/2f- l2, h,  8 - w2,
                        8 + 48/2f , 2*h,8 + w2 ),
                Block.box(8 + 48/2f-l3, 2*h, 8 - w3,
                        8 + 48/2f , 3*h,8 + w3 ),
                Block.box(8 + 48/2f-l4, 3*h, 8 - w4,
                        8 + 48/2f , 4.5f*h, 8 + w4));

        shapeWest = Shapes.or(this.shapeX,
                Block.box(8 - 48/2f, h,8 - w2 ,8 - 48/2f , 2*h, 8 + w2),
                Block.box(8 - 48/2f , 2*h, 8 - w3, 8 - 48/2f +l3, 3*h, 8 + w3),
                Block.box(8 - 48/2f, 3*h,8 - w4 , 8 - 48/2f +l4, 4.5f*h, 8 + w4));
    }


    @Override
    public VoxelShape getBaseShape(BlockState state) {
        return switch (state.getValue(FACING)){
            default -> shapeNorth;
            case SOUTH ->    shapeSouth;
            case EAST -> shapeEast;
            case WEST -> shapeWest;
        };
       // return (state.getValue(FACING).getAxis() == Direction.Axis.X) ? shapeX : shapeZ;
    }

}
