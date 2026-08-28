package net.larsmans.infinitybuttons.block.custom.secretbutton;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;

import net.larsmans.infinitybuttons.sounds.InfinityButtonsSounds;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

public class ChiseledNetherBrickSecretButton extends AbstractSecretButton {
    @Override
    public MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new ChiseledNetherBrickSecretButton(p, Blocks.CHISELED_NETHER_BRICKS));
    }
    public ChiseledNetherBrickSecretButton(BlockBehaviour.Properties properties, Block jadeBlock) {
        super (
                properties,
                Shapes.or(BOTTOM, TOP,
                        Block.box(13, 3, 0, 16, 13, 16),
                        Block.box(3, 3, 3, 13, 13, 16),
                        Block.box(0, 3, 0, 3, 13, 16)
                ),
                Shapes.or(BOTTOM, TOP,
                        Block.box(0, 3, 13, 16, 13, 16),
                        Block.box(0, 3, 3, 13, 13, 13),
                        Block.box(0, 3, 0, 16, 13, 3)
                ),
                Shapes.or(BOTTOM, TOP,
                        Block.box(0, 3, 0, 3, 13, 16),
                        Block.box(3, 3, 0, 13, 13, 13),
                        Block.box(13, 3, 0, 16, 13, 16)
                ),
                Shapes.or(BOTTOM, TOP,
                        Block.box(0, 3, 0, 16, 13, 3),
                        Block.box(3, 3, 3, 16, 13, 13),
                        Block.box(0, 3, 13, 16, 13, 16)
                ),
                Block.box(0, 0, 0, 16, 16, 16),
                jadeBlock
        );
    }

    // The bottom part that never moves
    private static final VoxelShape BOTTOM = Block.box(0, 0, 0, 16, 3, 16);
    // The top part that never moves
    private static final VoxelShape TOP = Block.box(0, 13, 0, 16, 16, 16);

    @Override
    protected SoundEvent getClickSound(boolean var1) {
        return InfinityButtonsSounds.STONE_SCRAPE.get();
    }
}
