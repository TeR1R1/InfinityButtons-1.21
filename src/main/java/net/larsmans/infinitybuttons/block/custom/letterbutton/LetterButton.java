package net.larsmans.infinitybuttons.block.custom.letterbutton;

import com.mojang.serialization.MapCodec;

import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.larsmans.infinitybuttons.block.custom.button.AbstractLeverableButton;
import net.larsmans.infinitybuttons.block.custom.button.LargeButtonShape;
import net.larsmans.infinitybuttons.network.OpenLetterScreenPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LetterButton extends AbstractLeverableButton {
    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return simpleCodec(p -> new LetterButton(p, false));
    }
    public static final EnumProperty<LetterButtonEnum> CHARACTER = EnumProperty.create("character", LetterButtonEnum.class);

    public LetterButton(Properties properties, boolean lever) {
        super(lever, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PRESSED, false)
                .setValue(FACE, AttachFace.FLOOR)
                .setValue(CHARACTER, LetterButtonEnum.NONE));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isSpectator()) {
            return InteractionResult.FAIL;
        }
        boolean adventure = player instanceof ServerPlayer serverPlayer
                && serverPlayer.gameMode.getGameModeForPlayer() == GameType.ADVENTURE;
        if (player.isShiftKeyDown() && !adventure) {
            openScreen(pos, player);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        openScreen(pos, placer);
    }

    public void openScreen(BlockPos pos, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new OpenLetterScreenPayload(pos));
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return LargeButtonShape.outlineShape(state);
    }

    @Override
    public int getPressTicks() {
        return 30;
    }

    @Override
    protected SoundEvent getClickSound(boolean pressed) {
        return SoundEvents.STONE_BUTTON_CLICK_ON;
    }

    public int getEnumId(BlockState state) {
        return state.getValue(CHARACTER).ordinal();
    }

    public void setState(BlockState state, Level level, BlockPos pos, LetterButtonEnum buttonEnum) {
        level.setBlock(pos, state.setValue(CHARACTER, buttonEnum), 3);
        this.updateNeighbors(state, level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CHARACTER);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        InfinityButtonsUtil.tooltip(tooltip, "letter_button");
    }
}
