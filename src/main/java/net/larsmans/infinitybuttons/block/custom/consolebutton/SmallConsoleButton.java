package net.larsmans.infinitybuttons.block.custom.consolebutton;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SmallConsoleButton extends ConsoleButton {
    protected static final VoxelShape CEILING_X_SHAPE = Block.box(5, 14, 3, 11, 16, 13);
    protected static final VoxelShape CEILING_Z_SHAPE = Block.box(3, 14, 5, 13, 16, 11);
    protected static final VoxelShape FLOOR_X_SHAPE = Block.box(5, 0, 3, 11, 2, 13);
    protected static final VoxelShape FLOOR_Z_SHAPE = Block.box(3, 0, 5, 13, 2, 11);
    protected static final VoxelShape NORTH_SHAPE = Block.box(3, 5, 14, 13, 11, 16);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(3, 5, 0, 13, 11, 2);
    protected static final VoxelShape WEST_SHAPE = Block.box(14, 5, 3, 16, 11, 13);
    protected static final VoxelShape EAST_SHAPE = Block.box(0, 5, 3, 2, 11, 13);

    public SmallConsoleButton(Properties properties, boolean lever) {
        super(properties, lever);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (state.getValue(FACE)) {
            case FLOOR -> direction.getAxis() == Direction.Axis.X ? FLOOR_X_SHAPE : FLOOR_Z_SHAPE;
            case WALL -> switch (direction) {
                case EAST -> EAST_SHAPE;
                case WEST -> WEST_SHAPE;
                case SOUTH -> SOUTH_SHAPE;
                default -> NORTH_SHAPE;
            };
            case CEILING -> direction.getAxis() == Direction.Axis.X ? CEILING_X_SHAPE : CEILING_Z_SHAPE;
        };
    }
}
