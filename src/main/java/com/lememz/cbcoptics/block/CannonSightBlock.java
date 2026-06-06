package com.lememz.cbcoptics.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CannonSightBlock extends TransparentBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final DirectionProperty LOOKING = DirectionProperty.create("looking");

    public CannonSightBlock() {
        super(BlockBehaviour.Properties.of().strength(1, 20).noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.DOWN)
                .setValue(LOOKING, Direction.NORTH)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, LOOKING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch(state.getValue(FACING)) {
            case DOWN -> Shapes.box(4/16., 0, 4/16., 12/16., 9/16., 12/16.);
            case UP -> Shapes.box(4/16., 7/16., 4/16., 12/16., 1, 12/16.);
            case NORTH -> Shapes.box(4/16., 4/16., 0, 12/16., 12/16., 9/16. );
            case SOUTH -> Shapes.box(4/16., 4/16., 7/16., 12/16., 12/16., 1);
            case WEST -> Shapes.box(0, 4/16., 4/16., 9/16., 12/16., 12/16.);
            case EAST -> Shapes.box(7/16., 4/16., 4/16., 1, 12/16., 12/16.);
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace().getOpposite();
        BlockState state = this.defaultBlockState().setValue(FACING, facing);
        Player player = context.getPlayer();
        if(player == null) {
            return state;
        }
        Direction finalDirection = Direction.DOWN;
        for(Direction dir : Direction.orderedByNearest(player)) {
            if(dir != facing && dir != facing.getOpposite()) {
                finalDirection = player.isShiftKeyDown() ? dir : dir.getOpposite();
                break;
            }
        }
        return state.setValue(LOOKING, finalDirection);
    }
}
