package net.larsmans.infinitybuttons.block.custom.button;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class StoneButton extends AbstractSmallButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new StoneButton(p, false));
    }
    public StoneButton(Properties properties, boolean large) {
        super(false, large, properties);
    }

    @Override
    public int getPressTicks() {
        return 20;
    }

    @Override
    protected SoundEvent getClickSound(boolean powered) {
        return powered ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}
