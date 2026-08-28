package net.larsmans.infinitybuttons;

import net.larsmans.infinitybuttons.particle.DiamondSparkleParticle;
import net.larsmans.infinitybuttons.particle.InfinityButtonsParticleTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = InfinityButtons.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InfinityButtonsClient {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
                InfinityButtonsParticleTypes.DIAMOND_SPARKLE.get(),
                DiamondSparkleParticle.Factory::new
        );
    }
}
