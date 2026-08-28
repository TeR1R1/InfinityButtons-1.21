package net.larsmans.infinitybuttons.network;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.larsmans.infinitybuttons.block.custom.letterbutton.LetterButton;
import net.larsmans.infinitybuttons.block.custom.letterbutton.gui.LetterButtonGui;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenLetterScreenPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<OpenLetterScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(InfinityButtons.MOD_ID, "letter_button_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenLetterScreenPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, OpenLetterScreenPayload::pos,
            OpenLetterScreenPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenLetterScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) {
                return;
            }
            BlockState state = mc.level.getBlockState(payload.pos());
            if (state.getBlock() instanceof LetterButton letterButton) {
                mc.setScreen(new LetterButtonGui(letterButton, state, mc.level, payload.pos()));
            }
        });
    }
}
