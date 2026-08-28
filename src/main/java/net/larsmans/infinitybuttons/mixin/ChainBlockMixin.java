package net.larsmans.infinitybuttons.mixin;

import net.larsmans.infinitybuttons.block.custom.LanternButton;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import static net.larsmans.infinitybuttons.InfinityButtonsUtil.checkChains;

@Mixin(ChainBlock.class)
public abstract class ChainBlockMixin extends RotatedPillarBlock {

    protected ChainBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return checkAround(state, level, pos) && direction == Direction.DOWN ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return checkAround(state, level, pos) && direction == Direction.DOWN ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public boolean checkAround(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(AXIS) != Direction.Axis.Y || level.getBlockState(pos.above()).getBlock() instanceof ChainBlock) {
            return false;
        }
        int i = 1;
        while (level.getBlockState(pos.below(i)).getBlock() instanceof ChainBlock) {
            if (level.getBlockState(pos.below(i)).getValue(AXIS) != Direction.Axis.Y) {
                return false;
            }
            i++;
        }
        BlockState blockState = level.getBlockState(pos.below(i));
        if (!(blockState.getBlock() instanceof LanternButton)) {
            return false;
        }
        return blockState.getValue(LanternButton.PRESSED);
    }

    // Update the top chain too if this chain is updated. If this is the top chain, update the redstone power positions
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean movedByPiston) {
        int distance = checkChains(level, pos);
        if (distance > 0) {
            level.neighborChanged(pos.above(distance), this, pos);
        } else {
            level.neighborChanged(pos.above(), this, pos);
            level.updateNeighborsAtExceptFromFacing(pos.above(), this, Direction.DOWN);
        }

        super.neighborChanged(state, level, pos, sourceBlock, sourcePos, movedByPiston);
    }

    // Update the redstone power positions if this is the top chain
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        int distance = checkChains(level, pos);
        if (distance == 0) {
            level.updateNeighborsAtExceptFromFacing(pos.above(), this, Direction.DOWN);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
