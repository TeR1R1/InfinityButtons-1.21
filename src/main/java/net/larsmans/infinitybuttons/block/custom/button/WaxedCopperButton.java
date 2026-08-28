package net.larsmans.infinitybuttons.block.custom.button;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.advancement.InfinityButtonsTriggers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class WaxedCopperButton extends AbstractSmallButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new WaxedCopperButton(p, false));
    }
    public WaxedCopperButton(Properties properties, boolean large) {
        super(false, large, properties);
    }

    @Override
    protected void playClickSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean pressed) {
        level.playSound(pressed ? player : null, pos, this.getClickSound(pressed), SoundSource.BLOCKS, 1F, pressed ? 0.6F : 0.5F);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() instanceof AxeItem && !(this instanceof CopperButton)) {
            if (state.getValue(PRESSED)) {
                return ItemInteractionResult.CONSUME;
            }
            return scrapeWax(state, level, pos, player, stack) == net.minecraft.world.InteractionResult.SUCCESS
                    || !level.isClientSide
                    ? ItemInteractionResult.sidedSuccess(level.isClientSide)
                    : ItemInteractionResult.SUCCESS;
        }
        if (stack.getItem() instanceof HoneyBottleItem && !(this instanceof CopperButton)) {
            return sticky(state, level, pos, player, hand, stack);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public net.minecraft.world.InteractionResult scrapeWax(BlockState state, Level level, BlockPos pos, Player player, ItemStack stack) {
        return WeatheringButton.getUnwaxed(state).map(unwaxed -> {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                InfinityButtonsTriggers.WAX_OFF_TRIGGER.get().trigger(serverPlayer);
                if (!player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, serverPlayer.serverLevel(), serverPlayer, item -> {});
                }
            }
            level.setBlock(pos, unwaxed, Block.UPDATE_ALL_IMMEDIATE);
            level.levelEvent(player, 3004, pos, 0);
            level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
            return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide);
        }).orElse(net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide));
    }

    public ItemInteractionResult sticky(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        return WeatheringButton.getSticky(state).map(sticky -> {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
            }
            if (!player.getAbilities().instabuild) {
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            }
            level.setBlock(pos, sticky, Block.UPDATE_ALL_IMMEDIATE);
            level.levelEvent(player, 3003, pos, 0);
            level.playSound(player, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }).orElse(ItemInteractionResult.sidedSuccess(level.isClientSide));
    }

    @Override
    public int getPressTicks() {
        return 50;
    }

    @Override
    protected SoundEvent getClickSound(boolean pressed) {
        return SoundEvents.COPPER_BREAK;
    }
}
