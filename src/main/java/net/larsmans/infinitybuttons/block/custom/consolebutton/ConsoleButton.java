package net.larsmans.infinitybuttons.block.custom.consolebutton;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.block.custom.button.AbstractLeverableButton;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ConsoleButton extends AbstractLeverableButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new ConsoleButton(p, false));
    }
    protected static final VoxelShape CEILING_X_SHAPE = Block.box(3, 14, 4, 13, 16, 12);
    protected static final VoxelShape CEILING_Z_SHAPE = Block.box(4, 14, 3, 12, 16, 13);
    protected static final VoxelShape FLOOR_X_SHAPE = Block.box(3, 0, 4, 13, 2, 12);
    protected static final VoxelShape FLOOR_Z_SHAPE = Block.box(4, 0, 3, 12, 2, 13);
    protected static final VoxelShape NORTH_SHAPE = Block.box(4, 3, 14, 12, 13, 16);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(4, 3, 0, 12, 13, 2);
    protected static final VoxelShape WEST_SHAPE = Block.box(14, 3, 4, 16, 13, 12);
    protected static final VoxelShape EAST_SHAPE = Block.box(0, 3, 4, 2, 13, 12);

    public ConsoleButton(Properties properties, boolean lever) {
        super(lever, properties);
    }

    @Override
    public int getPressTicks() {
        return 60;
    }

    @Override
    protected SoundEvent getClickSound(boolean pressed) {
        return SoundEvents.STONE_BUTTON_CLICK_ON;
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
