package net.larsmans.infinitybuttons.sounds;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InfinityButtonsSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, InfinityButtons.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ALARM = register("alarm");
    public static final DeferredHolder<SoundEvent, SoundEvent> STONE_SCRAPE = register("stone_scrape");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOOD_SCRAPE = register("wood_scrape");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOORBELL = register("doorbell");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(InfinityButtons.MOD_ID, name)));
    }
}
