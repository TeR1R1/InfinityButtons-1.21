package net.larsmans.infinitybuttons.block.custom.secretbutton;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;

import net.larsmans.infinitybuttons.sounds.InfinityButtonsSounds;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FullBlockBrickSecretButton extends AbstractSecretButton {
    @Override
    public MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new FullBlockBrickSecretButton(p, Blocks.STONE_BRICKS));
    }
    public FullBlockBrickSecretButton(BlockBehaviour.Properties properties, Block jadeBlock) {
        super (
                properties,
                FULL,
                FULL,
                FULL,
                FULL,
                FULL,
                jadeBlock
        );
    }

    // Full block because I am too lazy to do the whole voxelshape thing
    private static final VoxelShape FULL = Block.box(0, 0, 0, 16, 16, 16);

    @Override
    protected SoundEvent getClickSound(boolean var1) {
        return InfinityButtonsSounds.STONE_SCRAPE.get();
    }
}
