package net.larsmans.infinitybuttons.particle;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InfinityButtonsParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, InfinityButtons.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DIAMOND_SPARKLE =
            PARTICLES.register("diamond_sparkle", () -> new SimpleParticleType(false));
}
