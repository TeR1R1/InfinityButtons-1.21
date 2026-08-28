package net.larsmans.infinitybuttons.network;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.larsmans.infinitybuttons.block.custom.letterbutton.LetterButton;
import net.larsmans.infinitybuttons.block.custom.letterbutton.LetterButtonEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LetterButtonSelectPayload(BlockPos pos, LetterButtonEnum character) implements CustomPacketPayload {
    public static final Type<LetterButtonSelectPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(InfinityButtons.MOD_ID, "letter_button_block"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LetterButtonSelectPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, LetterButtonSelectPayload::pos,
            ByteBufCodecs.idMapper(i -> LetterButtonEnum.values()[i], Enum::ordinal), LetterButtonSelectPayload::character,
            LetterButtonSelectPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(LetterButtonSelectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            BlockState state = level.getBlockState(payload.pos());
            if (state.getBlock() instanceof LetterButton) {
                level.setBlock(payload.pos(), state.setValue(LetterButton.CHARACTER, payload.character()), 3);
            }
        });
    }
}
