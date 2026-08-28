package net.larsmans.infinitybuttons.block.custom.torch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class WallTorchLever extends WallTorchButton {
    public WallTorchLever(Properties properties, ParticleOptions particle, Block jadeBlock) {
        super(properties, particle, jadeBlock);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(PRESSED)) {
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
        level.setBlock(pos, state.setValue(PRESSED, true), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
    }

    public void powerOff(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(PRESSED, false), Block.UPDATE_ALL);
        this.updateNeighbors(state, level, pos);
    }
}
