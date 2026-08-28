package net.larsmans.infinitybuttons.block.custom.torch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class RedstoneWallTorchLever extends RedstoneWallTorchButton {
    public RedstoneWallTorchLever(Properties properties, Block jadeBlock) {
        super(properties, jadeBlock);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValue(LIT)) {
            this.powerOff(state, level, pos);
            this.playClickSound(player, level, pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DEACTIVATE, pos);
        } else {
            this.powerOn(state, level, pos);
            this.playClickSound(player, level, pos, true);
            level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void powerOn(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
    }

    public void powerOff(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
    }
}
