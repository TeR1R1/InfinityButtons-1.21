package net.larsmans.infinitybuttons.block.custom.button;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;

import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.larsmans.infinitybuttons.config.InfinityButtonsConfig;
import net.larsmans.infinitybuttons.particle.InfinityButtonsParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class DiamondButton extends AbstractSmallButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new DiamondButton(p, false));
    }
    private final boolean large;

    public DiamondButton(Properties properties, boolean large) {
        super(false, large, properties);
        this.large = large;
    }

    @Override
    public int getPressTicks() {
        return 20;
    }

    @Override
    protected SoundEvent getClickSound(boolean pressed) {
        return pressed ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (InfinityButtonsConfig.DIAMOND_PARTICLES.get() && random.nextInt(3) == 0) {
            if (large) {
                switch (state.getValue(FACE)) {
                    case FLOOR -> addParticle(3, 10, 2, 1, 3, 10, level, pos, random);
                    case WALL -> {
                        switch (state.getValue(FACING)) {
                            case NORTH -> addParticle(3, 10, 3, 10, 13, 1, level, pos, random);
                            case EAST -> addParticle(2, 1, 3, 10, 3, 10, level, pos, random);
                            case SOUTH -> addParticle(3, 10, 3, 10, 2, 1, level, pos, random);
                            case WEST -> addParticle(13, 1, 3, 10, 3, 10, level, pos, random);
                        }
                    }
                    case CEILING -> addParticle(3, 10, 13, 1, 3, 10, level, pos, random);
                }
            } else {
                switch (state.getValue(FACE)) {
                    case FLOOR -> {
                        switch (state.getValue(FACING)) {
                            case NORTH, SOUTH -> addParticle(4, 8, 2, 1, 5, 6, level, pos, random);
                            case EAST, WEST -> addParticle(5, 6, 2, 1, 4, 8, level, pos, random);
                        }
                    }
                    case WALL -> {
                        switch (state.getValue(FACING)) {
                            case NORTH -> addParticle(4, 8, 5, 6, 13, 1, level, pos, random);
                            case EAST -> addParticle(2, 1, 5, 6, 4, 8, level, pos, random);
                            case SOUTH -> addParticle(4, 8, 5, 6, 2, 1, level, pos, random);
                            case WEST -> addParticle(13, 1, 5, 6, 4, 8, level, pos, random);
                        }
                    }
                    case CEILING -> {
                        switch (state.getValue(FACING)) {
                            case NORTH, SOUTH -> addParticle(4, 8, 13, 1, 5, 6, level, pos, random);
                            case EAST, WEST -> addParticle(5, 6, 13, 1, 4, 8, level, pos, random);
                        }
                    }
                }
            }
        }
    }

    public void addParticle(int x1, int x2, int y1, int y2, int z1, int z2, Level level, BlockPos pos, RandomSource random) {
        level.addParticle(InfinityButtonsParticleTypes.DIAMOND_SPARKLE.get(),
                pos.getX() + (double) x1 / 16 + random.nextFloat() * (double) x2 / 16,
                pos.getY() + (double) y1 / 16 + random.nextFloat() * (double) y2 / 16,
                pos.getZ() + (double) z1 / 16 + random.nextFloat() * (double) z2 / 16,
                0, 0, 0);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        InfinityButtonsUtil.tooltip(tooltip, "diamond_button");
    }
}
