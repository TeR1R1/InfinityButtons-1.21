package net.larsmans.infinitybuttons.block.custom.button;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractLeverableButton extends AbstractButton {
    public final boolean lever;

    public AbstractLeverableButton(boolean lever, Properties properties) {
        super(false, properties);
        this.lever = lever;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (lever) {
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
            this.playClickSound(player, level, pos, true);
            level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void powerOn(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(PRESSED, true), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
        if (!lever) {
            level.scheduleTick(pos, this, this.getPressTicks());
        }
    }

    public void powerOff(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(PRESSED, false), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
    }
}
