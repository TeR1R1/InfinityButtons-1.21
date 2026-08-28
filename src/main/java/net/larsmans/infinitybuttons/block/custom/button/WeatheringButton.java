package net.larsmans.infinitybuttons.block.custom.button;

import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface WeatheringButton extends WeatheringCopper {
    static Optional<BlockState> getWaxed(BlockState state) {
        InfinityButtonsUtil.buildWax();
        return Optional.ofNullable(InfinityButtonsUtil.WAX_ON_BY_BLOCK.get().get(state.getBlock()))
                .map(block -> block.withPropertiesOf(state));
    }

    static Optional<BlockState> getUnwaxed(BlockState state) {
        InfinityButtonsUtil.buildWax();
        return Optional.ofNullable(InfinityButtonsUtil.WAX_OFF_BY_BLOCK.get().get(state.getBlock()))
                .map(block -> block.withPropertiesOf(state));
    }

    static Optional<BlockState> getSticky(BlockState state) {
        InfinityButtonsUtil.buildSticky();
        return Optional.ofNullable(InfinityButtonsUtil.STICKY_ON_BY_BLOCK.get().get(state.getBlock()))
                .map(block -> block.withPropertiesOf(state));
    }

    static Optional<BlockState> getUnsticky(BlockState state) {
        InfinityButtonsUtil.buildSticky();
        return Optional.ofNullable(InfinityButtonsUtil.STICKY_OFF_BY_BLOCK.get().get(state.getBlock()))
                .map(block -> block.withPropertiesOf(state));
    }

    @Override
    default @NotNull Optional<BlockState> getNext(BlockState state) {
        return getNext(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    static Optional<Block> getNext(Block block) {
        InfinityButtonsUtil.buildNext();
        return Optional.ofNullable(InfinityButtonsUtil.NEXT_BY_BLOCK.get().get(block));
    }

    static Optional<Block> getPrevious(Block block) {
        InfinityButtonsUtil.buildNext();
        return Optional.ofNullable(InfinityButtonsUtil.PREVIOUS_BY_BLOCK.get().get(block));
    }

    static Optional<BlockState> getPrevious(BlockState state) {
        return getPrevious(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    default InteractionResult wax(BlockState state, Level level, BlockPos pos, Player player, ItemStack stack) {
        return getWaxed(state).map(waxed -> {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.setBlock(pos, waxed, Block.UPDATE_ALL_IMMEDIATE);
            level.levelEvent(player, 3003, pos, 0);
            level.playSound(player, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }).orElse(InteractionResult.sidedSuccess(level.isClientSide));
    }

    default InteractionResult scrape(BlockState state, Level level, BlockPos pos, Player player, ItemStack stack) {
        return getPrevious(state).map(previous -> {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
            }
            if (!player.getAbilities().instabuild && player instanceof ServerPlayer serverPlayer) {
                stack.hurtAndBreak(1, serverPlayer.serverLevel(), serverPlayer, item -> {});
            }
            level.setBlock(pos, previous, Block.UPDATE_ALL_IMMEDIATE);
            level.levelEvent(player, 3005, pos, 0);
            level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }).orElse(InteractionResult.sidedSuccess(level.isClientSide));
    }
}
