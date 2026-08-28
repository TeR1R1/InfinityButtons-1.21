package net.larsmans.infinitybuttons.block.custom.secretbutton;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BookshelfSecretButton extends AbstractSecretButton {
    @Override
    public MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new BookshelfSecretButton(p, Blocks.BOOKSHELF));
    }
    public BookshelfSecretButton(BlockBehaviour.Properties properties, Block jadeBlock) {
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
        // Credits to anne for suggesting this immensely satisfying sound
        return SoundEvents.BAMBOO_PLACE;
    }
}
