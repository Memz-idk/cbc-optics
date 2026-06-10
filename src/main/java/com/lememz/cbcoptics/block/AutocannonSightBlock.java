package com.lememz.cbcoptics.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AutocannonSightBlock extends SightBlock {

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch(state.getValue(FACING)) {
            case DOWN -> Shapes.box(5/16., 0, 5/16., 11/16., 6/16., 11/16.);
            case UP -> Shapes.box(5/16., 10/16., 5/16., 11/16., 1, 11/16.);
            case NORTH -> Shapes.box(5/16., 5/16., 0, 11/16., 11/16., 6/16. );
            case SOUTH -> Shapes.box(5/16., 5/16., 10/16., 11/16., 11/16., 1);
            case WEST -> Shapes.box(0, 5/16., 5/16., 6/16., 11/16., 11/16.);
            case EAST -> Shapes.box(10/16., 5/16., 5/16., 1, 11/16., 11/16.);
        };
    }
}
