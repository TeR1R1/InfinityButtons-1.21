package net.larsmans.infinitybuttons.block.custom.button;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CopperButton extends WaxedCopperButton implements WeatheringButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new CopperButton(p, false, WeatheringCopper.WeatherState.UNAFFECTED));
    }
    private final WeatheringCopper.WeatherState weatherState;

    public CopperButton(Properties properties, boolean large, WeatheringCopper.WeatherState weatherState) {
        super(properties, large);
        this.weatherState = weatherState;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(PRESSED)) {
            return ItemInteractionResult.CONSUME;
        }
        if (stack.getItem() instanceof HoneycombItem) {
            var result = wax(state, level, pos, player, stack);
            return result.consumesAction() ? ItemInteractionResult.sidedSuccess(level.isClientSide) : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (stack.getItem() instanceof AxeItem && getAge() != WeatheringCopper.WeatherState.UNAFFECTED) {
            var result = scrape(state, level, pos, player, stack);
            return result.consumesAction() ? ItemInteractionResult.sidedSuccess(level.isClientSide) : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringButton.getNext(state.getBlock()).isPresent() && !state.getValue(PRESSED);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }
}
