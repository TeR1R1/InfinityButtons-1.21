package net.larsmans.infinitybuttons.network;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.larsmans.infinitybuttons.config.AlarmEnum;
import net.larsmans.infinitybuttons.config.InfinityButtonsConfig;
import net.larsmans.infinitybuttons.sounds.InfinityButtonsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AlarmPayload(BlockPos pos, AlarmEnum alarm) implements CustomPacketPayload {
    public static final Type<AlarmPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(InfinityButtons.MOD_ID, "alarm"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlarmPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, AlarmPayload::pos,
            ByteBufCodecs.idMapper(i -> AlarmEnum.values()[i], Enum::ordinal), AlarmPayload::alarm,
            AlarmPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AlarmPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (InfinityButtonsConfig.MUTE_ALARM_SOUND.get()) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) {
                return;
            }
            if (payload.alarm() == AlarmEnum.GLOBAL) {
                Camera cam = mc.gameRenderer.getMainCamera();
                Vec3 center = Vec3.atCenterOf(payload.pos());
                if (cam.isInitialized()) {
                    float chunkDistance = (float) cam.getPosition().distanceTo(center) / 16.0f;
                    mc.level.playLocalSound(center.x, center.y, center.z, InfinityButtonsSounds.ALARM.get(),
                            SoundSource.BLOCKS, chunkDistance * 1.3f + 20f, 1.0f, false);
                }
            } else {
                mc.level.playSound(mc.player, payload.pos(), InfinityButtonsSounds.ALARM.get(), SoundSource.BLOCKS,
                        InfinityButtonsConfig.ALARM_SOUND_RANGE.get().floatValue(), 1);
            }
        });
    }
}
