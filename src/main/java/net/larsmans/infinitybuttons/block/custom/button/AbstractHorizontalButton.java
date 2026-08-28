package net.larsmans.infinitybuttons.block.custom.button;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHorizontalButton extends HorizontalDirectionalBlock {
    public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");

    public final VoxelShape NORTH_SHAPE;
    public final VoxelShape EAST_SHAPE;
    public final VoxelShape SOUTH_SHAPE;
    public final VoxelShape WEST_SHAPE;
    public final VoxelShape NORTH_PRESSED_SHAPE;
    public final VoxelShape EAST_PRESSED_SHAPE;
    public final VoxelShape SOUTH_PRESSED_SHAPE;
    public final VoxelShape WEST_PRESSED_SHAPE;

    protected AbstractHorizontalButton(Properties properties, VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west,
                                       VoxelShape northPressed, VoxelShape eastPressed, VoxelShape southPressed, VoxelShape westPressed) {
        super(properties);
        NORTH_SHAPE = north;
        EAST_SHAPE = east;
        SOUTH_SHAPE = south;
        WEST_SHAPE = west;
        NORTH_PRESSED_SHAPE = northPressed;
        EAST_PRESSED_SHAPE = eastPressed;
        SOUTH_PRESSED_SHAPE = southPressed;
        WEST_PRESSED_SHAPE = westPressed;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PRESSED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean pressed = state.getValue(PRESSED);
        return switch (state.getValue(FACING)) {
            case NORTH -> pressed ? NORTH_PRESSED_SHAPE : NORTH_SHAPE;
            case EAST -> pressed ? EAST_PRESSED_SHAPE : EAST_SHAPE;
            case SOUTH -> pressed ? SOUTH_PRESSED_SHAPE : SOUTH_SHAPE;
            default -> pressed ? WEST_PRESSED_SHAPE : WEST_SHAPE;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PRESSED, FACING);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(PRESSED)) {
            return InteractionResult.CONSUME;
        }
        this.powerOn(state, level, pos);
        this.playClickSound(player, level, pos, true);
        level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void powerOn(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(PRESSED, true), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
        level.scheduleTick(pos, this, this.getPressTicks());
    }

    public abstract int getPressTicks();

    protected void playClickSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
        level.playSound(pressed ? player : null, pos, this.getClickSound(pressed), SoundSource.BLOCKS, 0.3f, pressed ? 0.6f : 0.5f);
    }

    protected abstract SoundEvent getClickSound(boolean pressed);

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (movedByPiston || state.is(newState.getBlock())) {
            return;
        }
        if (state.getValue(PRESSED)) {
            this.updateNeighbors(state, level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(PRESSED) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (state.getValue(PRESSED) && state.getValue(FACING) == direction) {
            return 15;
        }
        return 0;
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PRESSED)) {
            level.setBlock(pos, state.setValue(PRESSED, false), Block.UPDATE_ALL);
            this.updateNeighbors(state, level, pos);
            this.playClickSound(null, level, pos, false);
            level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
        }
    }

    public void updateNeighbors(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), this);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }
}
