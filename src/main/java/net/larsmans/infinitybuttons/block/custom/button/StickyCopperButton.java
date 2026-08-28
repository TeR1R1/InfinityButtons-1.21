package net.larsmans.infinitybuttons.block.custom.button;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StickyCopperButton extends AbstractSmallButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new StickyCopperButton(p, false));
    }
    public StickyCopperButton(Properties properties, boolean large) {
        super(false, large, properties);
    }

    @Override
    public int getPressTicks() {
        return 0;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof AxeItem) {
            return WeatheringButton.getUnsticky(state).map(unsticky -> {
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                    if (!player.getAbilities().instabuild) {
                        stack.hurtAndBreak(1, serverPlayer.serverLevel(), serverPlayer, item -> {});
                    }
                }
                level.setBlock(pos, unsticky.setValue(PRESSED, false), Block.UPDATE_ALL_IMMEDIATE);
                level.levelEvent(player, 3004, pos, 0);
                level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }).orElse(ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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

    @Override
    protected void playClickSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean powered) {
        level.playSound(powered ? player : null, pos, this.getClickSound(powered), SoundSource.BLOCKS, 1f, powered ? 0.6f : 0.5f);
    }

    @Override
    protected SoundEvent getClickSound(boolean powered) {
        return SoundEvents.COPPER_BREAK;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        InfinityButtonsUtil.tooltip(tooltip, "sticky_copper_button");
    }
}
