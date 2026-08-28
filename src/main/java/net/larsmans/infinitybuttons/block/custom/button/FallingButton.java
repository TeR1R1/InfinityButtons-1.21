package net.larsmans.infinitybuttons.block.custom.button;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class FallingButton extends AbstractSmallButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new FallingButton(false, p, false));
    }
    public boolean gravel;

    public FallingButton(boolean gravel, Properties properties, boolean large) {
        super(false, large, properties);
        this.gravel = gravel;
    }

    @Override
    public int getPressTicks() {
        return 10;
    }

    @Override
    protected SoundEvent getClickSound(boolean pressed) {
        return gravel ? SoundEvents.GRAVEL_BREAK : SoundEvents.SAND_BREAK;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PRESSED)) {
            level.setBlock(pos, state.setValue(PRESSED, false), Block.UPDATE_ALL);
            this.updateNeighbors(state, level, pos);
            this.playClickSound(null, level, pos, false);
            level.destroyBlock(pos, false);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        InfinityButtonsUtil.tooltip(tooltip, "falling_button");
    }
}
