package com.lememz.cbcoptics.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SightBlock extends TransparentBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final DirectionProperty LOOKING = DirectionProperty.create("looking");

    public SightBlock() {
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
