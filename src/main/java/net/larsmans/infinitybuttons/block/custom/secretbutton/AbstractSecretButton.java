package net.larsmans.infinitybuttons.block.custom.secretbutton;

import net.larsmans.infinitybuttons.block.custom.button.AbstractHorizontalButton;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractSecretButton extends AbstractHorizontalButton {
    public final VoxelShape NORTH_SHAPE;
    public final VoxelShape EAST_SHAPE;
    public final VoxelShape SOUTH_SHAPE;
    public final VoxelShape WEST_SHAPE;
    public final VoxelShape OFF_SHAPE;
    public final Block jadeBlock;

    protected AbstractSecretButton(Properties properties, VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west, VoxelShape off, Block jadeBlock) {
        super(properties, off, off, off, off, north, east, south, west);
        NORTH_SHAPE = north;
        EAST_SHAPE = east;
        SOUTH_SHAPE = south;
        WEST_SHAPE = west;
        OFF_SHAPE = off;
        this.jadeBlock = jadeBlock;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!state.getValue(PRESSED) && hit.getDirection() == state.getValue(FACING)) {
            this.powerOn(state, level, pos);
            this.playClickSound(player, level, pos, true);
            level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void playClickSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
        level.playSound(pressed ? player : null, pos, this.getClickSound(pressed), SoundSource.BLOCKS, 1f, pressed ? 0.6f : 0.5f);
    }

    @Override
    public int getPressTicks() {
        return 50;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, net.minecraft.world.level.LevelReader level, BlockPos pos) {
        return this.jadeBlock == net.minecraft.world.level.block.Blocks.BOOKSHELF ? 1.0f : 0.0f;
    }
}
