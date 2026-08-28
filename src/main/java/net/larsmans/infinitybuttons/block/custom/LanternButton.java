package net.larsmans.infinitybuttons.block.custom;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.larsmans.infinitybuttons.InfinityButtonsUtil.checkChains;
import static net.minecraft.world.level.block.LanternBlock.WATERLOGGED;

public class LanternButton extends Block implements SimpleWaterloggedBlock {
    @Override
    public MapCodec<? extends Block> codec() {
        return simpleCodec(p -> new LanternButton(p, false, Blocks.LANTERN));
    }
    public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");
    private static final VoxelShape HANGING_SHAPE = Shapes.or(Block.box(5.0, 1.0, 5.0, 11.0, 8.0, 11.0), Block.box(6.0, 8.0, 6.0, 10.0, 10.0, 10.0));
    public static final VoxelShape SHAPE_PRESSED = HANGING_SHAPE.move(0, -1.0 / 16.0, 0);
    private final boolean isLever;
    public final Block jadeBlock;

    public LanternButton(Properties properties, boolean isLever, Block jadeBlock) {
        super(properties);
        this.isLever = isLever;
        this.jadeBlock = jadeBlock;
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(PRESSED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PRESSED) ? SHAPE_PRESSED : HANGING_SHAPE;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction != Direction.UP) {
                continue;
            }
            return this.defaultBlockState().setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, PRESSED);
    }

    protected int getPressTicks() {
        return 30;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return direction == Direction.UP && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (isLever) {
            if (state.getValue(PRESSED)) {
                this.powerOff(state, level, pos);
                this.playClickSound(player, level, pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DEACTIVATE, pos);
            } else {
                this.powerOn(state, level, pos);
                this.playClickSound(player, level, pos, true);
                level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
            }
        } else {
            if (state.getValue(PRESSED)) {
                return InteractionResult.CONSUME;
            }
            this.powerOn(state, level, pos);
            level.scheduleTick(pos, this, this.getPressTicks());
            this.playClickSound(player, level, pos, true);
            level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void powerOn(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(PRESSED, true), Block.UPDATE_ALL);
        this.updateNeighbors(level, pos);
    }

    public void powerOff(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(PRESSED, false), Block.UPDATE_ALL);
        this.updateNeighbors(level, pos);
    }

    protected void playClickSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
        level.playSound(pressed ? player : null, pos, this.getClickSound(), SoundSource.BLOCKS, 0.3f, pressed ? 0.6f : 0.5f);
    }

    protected SoundEvent getClickSound() {
        return SoundEvents.STONE_BUTTON_CLICK_OFF;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (movedByPiston || state.is(newState.getBlock())) {
            return;
        }
        if (state.getValue(PRESSED)) {
            this.updateNeighbors(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(PRESSED) ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(PRESSED) && direction == Direction.DOWN ? 15 : 0;
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(PRESSED)) {
            return;
        }
        level.setBlock(pos, state.setValue(PRESSED, false), Block.UPDATE_ALL);
        this.updateNeighbors(level, pos);
        this.playClickSound(null, level, pos, false);
        level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
    }

    public void updateNeighbors(Level level, BlockPos pos) {
        int distance = checkChains(level, pos);
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.above(distance), this);
        level.updateNeighborsAt(pos.above(distance + 1), this);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        InfinityButtonsUtil.tooltip(tooltip, "lantern_button");
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.relative(Direction.UP), Direction.DOWN);
    }
}
