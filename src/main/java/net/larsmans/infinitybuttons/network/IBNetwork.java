package net.larsmans.infinitybuttons.network;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = InfinityButtons.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class IBNetwork {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(LetterButtonSelectPayload.TYPE, LetterButtonSelectPayload.STREAM_CODEC, LetterButtonSelectPayload::handle);
        registrar.playToClient(OpenLetterScreenPayload.TYPE, OpenLetterScreenPayload.STREAM_CODEC, OpenLetterScreenPayload::handle);
        registrar.playToClient(AlarmPayload.TYPE, AlarmPayload.STREAM_CODEC, AlarmPayload::handle);
    }
}
