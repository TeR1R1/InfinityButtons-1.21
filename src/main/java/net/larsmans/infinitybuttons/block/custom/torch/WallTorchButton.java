package net.larsmans.infinitybuttons.block.custom.torch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WallTorchButton extends TorchButton {
    private static final Map<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0),
            Direction.SOUTH, Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0),
            Direction.WEST, Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5),
            Direction.EAST, Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5)));

    public WallTorchButton(Properties properties, ParticleOptions particle, Block jadeBlock) {
        super(properties, particle, jadeBlock);
    }

    @Override
    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOUNDING_SHAPES.get(state.getValue(FACING));
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

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    @Override
    public void updateNeighbors(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), this);
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(PRESSED) && state.getValue(FACING) == direction ? 15 : 0;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction direction = state.getValue(FACING);
        if (state.getValue(PRESSED)) {
            double d = pos.getX() + 0.5;
            double e = pos.getY() + 0.6;
            double f = pos.getZ() + 0.5;
            Direction opposite = direction.getOpposite();
            level.addParticle(ParticleTypes.SMOKE, d + 0.05 * opposite.getStepX(), e + 0.15, f + 0.05 * opposite.getStepZ(), 0.0, 0.0, 0.0);
            level.addParticle(this.particle, d + 0.05 * opposite.getStepX(), e + 0.15, f + 0.05 * opposite.getStepZ(), 0.0, 0.0, 0.0);
        } else {
            double d = pos.getX() + 0.5;
            double e = pos.getY() + 0.7;
            double f = pos.getZ() + 0.5;
            Direction opposite = direction.getOpposite();
            level.addParticle(ParticleTypes.SMOKE, d + 0.27 * opposite.getStepX(), e + 0.22, f + 0.27 * opposite.getStepZ(), 0.0, 0.0, 0.0);
            level.addParticle(this.particle, d + 0.27 * opposite.getStepX(), e + 0.22, f + 0.27 * opposite.getStepZ(), 0.0, 0.0, 0.0);
        }
    }
}
