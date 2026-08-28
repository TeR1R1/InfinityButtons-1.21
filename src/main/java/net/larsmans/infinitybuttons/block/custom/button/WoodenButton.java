package net.larsmans.infinitybuttons.block.custom.button;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class WoodenButton extends AbstractSmallButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new WoodenButton(p, false));
    }
    public WoodenButton(Properties properties, boolean large) {
        super(true, large, properties);
    }

    @Override
    public int getPressTicks() {
        return 30;
    }

    @Override
    protected SoundEvent getClickSound(boolean powered) {
        return powered ? SoundEvents.WOODEN_BUTTON_CLICK_ON : SoundEvents.WOODEN_BUTTON_CLICK_OFF;
    }
}
