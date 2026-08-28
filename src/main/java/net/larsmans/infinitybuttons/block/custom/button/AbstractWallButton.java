package net.larsmans.infinitybuttons.block.custom.button;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractWallButton extends AbstractHorizontalButton {
    protected AbstractWallButton(Properties properties, VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west,
                                 VoxelShape northPressed, VoxelShape eastPressed, VoxelShape southPressed, VoxelShape westPressed) {
        super(properties, north, east, south, west, northPressed, eastPressed, southPressed, westPressed);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos behind = pos.relative(direction.getOpposite());
        return level.getBlockState(behind).isFaceSturdy(level, behind, direction);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        for (Direction direction : context.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal()) {
                continue;
            }
            state = state.setValue(FACING, direction.getOpposite());
            if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                return state;
            }
        }
        return null;
    }
}
