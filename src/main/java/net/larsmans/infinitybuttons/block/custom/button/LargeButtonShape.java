package net.larsmans.infinitybuttons.block.custom.button;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class LargeButtonShape extends AbstractButton {
    private static final VoxelShape FLOOR_SHAPE = Block.box(4, 0, 4, 12, 2, 12);
    private static final VoxelShape FLOOR_PRESSED_SHAPE = Block.box(4, 0, 4, 12, 1, 12);
    private static final VoxelShape NORTH_SHAPE = Block.box(4, 4, 14, 12, 12, 16);
    private static final VoxelShape NORTH_PRESSED_SHAPE = Block.box(4, 4, 15, 12, 12, 16);
    private static final VoxelShape EAST_SHAPE = Block.box(0, 4, 4, 2, 12, 12);
    private static final VoxelShape EAST_PRESSED_SHAPE = Block.box(0, 4, 4, 1, 12, 12);
    private static final VoxelShape SOUTH_SHAPE = Block.box(4, 4, 0, 12, 12, 2);
    private static final VoxelShape SOUTH_PRESSED_SHAPE = Block.box(4, 4, 0, 12, 12, 1);
    private static final VoxelShape WEST_SHAPE = Block.box(14, 4, 4, 16, 12, 12);
    private static final VoxelShape WEST_PRESSED_SHAPE = Block.box(15, 4, 4, 16, 12, 12);
    private static final VoxelShape CEILING_SHAPE = Block.box(4, 14, 4, 12, 16, 12);
    private static final VoxelShape CEILING_PRESSED_SHAPE = Block.box(4, 15, 4, 12, 16, 12);

    public LargeButtonShape(boolean projectile, Properties properties) {
        super(projectile, properties);
    }

    public static VoxelShape outlineShape(BlockState state) {
        Direction direction = state.getValue(FACING);
        boolean pressed = state.getValue(PRESSED);
        return switch (state.getValue(FACE)) {
            case FLOOR -> pressed ? FLOOR_PRESSED_SHAPE : FLOOR_SHAPE;
            case WALL -> switch (direction) {
                case EAST -> pressed ? EAST_PRESSED_SHAPE : EAST_SHAPE;
                case WEST -> pressed ? WEST_PRESSED_SHAPE : WEST_SHAPE;
                case SOUTH -> pressed ? SOUTH_PRESSED_SHAPE : SOUTH_SHAPE;
                default -> pressed ? NORTH_PRESSED_SHAPE : NORTH_SHAPE;
            };
            case CEILING -> pressed ? CEILING_PRESSED_SHAPE : CEILING_SHAPE;
        };
    }
}
