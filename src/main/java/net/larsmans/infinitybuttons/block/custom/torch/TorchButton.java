package net.larsmans.infinitybuttons.block.custom.torch;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.block.custom.button.AbstractHorizontalButton;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TorchButton extends AbstractHorizontalButton {
    @Override
    public MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new TorchButton(p, ParticleTypes.FLAME, Blocks.TORCH));
    }
    protected static final VoxelShape BOUNDING_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);
    protected final ParticleOptions particle;
    public final Block jadeBlock;

    public TorchButton(Properties properties, ParticleOptions particle, Block jadeBlock) {
        super(properties, BOUNDING_SHAPE, BOUNDING_SHAPE, BOUNDING_SHAPE, BOUNDING_SHAPE, BOUNDING_SHAPE, BOUNDING_SHAPE, BOUNDING_SHAPE, BOUNDING_SHAPE);
        this.particle = particle;
        this.jadeBlock = jadeBlock;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOUNDING_SHAPE;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction direction = state.getValue(FACING);
        if (state.getValue(PRESSED)) {
            double d = pos.getX() + 0.5;
            double e = pos.getY() + 0.63;
            double f = pos.getZ() + 0.5;
            Direction opposite = direction.getOpposite();
            level.addParticle(ParticleTypes.SMOKE, d + 0.23 * opposite.getStepX(), e, f + 0.23 * opposite.getStepZ(), 0.0, 0.0, 0.0);
            level.addParticle(this.particle, d + 0.23 * opposite.getStepX(), e, f + 0.23 * opposite.getStepZ(), 0.0, 0.0, 0.0);
        } else {
            double d = pos.getX() + 0.5;
            double e = pos.getY() + 0.7;
            double f = pos.getZ() + 0.5;
            level.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            level.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public int getPressTicks() {
        return 50;
    }

    @Override
    protected SoundEvent getClickSound(boolean pressed) {
        return SoundEvents.WOODEN_BUTTON_CLICK_ON;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(PRESSED) && Direction.DOWN.getOpposite() == direction ? 15 : 0;
    }

    @Override
    public void updateNeighbors(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(Direction.DOWN), this);
    }
}
