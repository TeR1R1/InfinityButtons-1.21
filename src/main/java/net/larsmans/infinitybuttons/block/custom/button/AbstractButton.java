package net.larsmans.infinitybuttons.block.custom.button;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractButton extends FaceAttachedHorizontalDirectionalBlock {
    public static final BooleanProperty PRESSED = BooleanProperty.create("pressed");

    protected final boolean projectile;

    protected AbstractButton(boolean projectile, Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PRESSED, false)
                .setValue(FACE, AttachFace.FLOOR));
        this.projectile = projectile;
    }

    public abstract int getPressTicks();

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
        if (state.getValue(PRESSED) && getConnectedDirection(state) == direction) {
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
        if (!state.getValue(PRESSED)) {
            return;
        }
        if (this.projectile) {
            this.tryPowerWithProjectiles(state, level, pos);
        } else {
            level.setBlock(pos, state.setValue(PRESSED, false), Block.UPDATE_ALL);
            this.updateNeighbors(state, level, pos);
            this.playClickSound(null, level, pos, false);
            level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !this.projectile || state.getValue(PRESSED)) {
            return;
        }
        this.tryPowerWithProjectiles(state, level, pos);
    }

    private void tryPowerWithProjectiles(BlockState state, Level level, BlockPos pos) {
        List<AbstractArrow> list = level.getEntitiesOfClass(AbstractArrow.class, state.getShape(level, pos).bounds().move(pos));
        boolean bl = !list.isEmpty();
        boolean pressed = state.getValue(PRESSED);
        if (bl != pressed) {
            level.setBlock(pos, state.setValue(PRESSED, bl), Block.UPDATE_ALL);
            this.updateNeighbors(state, level, pos);
            this.playClickSound(null, level, pos, bl);
            level.gameEvent(list.stream().findFirst().orElse(null), bl ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        }
        if (bl) {
            level.scheduleTick(pos, this, this.getPressTicks());
        }
    }

    public void updateNeighbors(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PRESSED, FACE);
    }
}
