package net.larsmans.infinitybuttons.advancement;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InfinityButtonsTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, InfinityButtons.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, SafetyTrigger> SAFETY_TRIGGER =
            TRIGGERS.register("safety_button_head", SafetyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, EmergencyTrigger> EMERGENCY_TRIGGER =
            TRIGGERS.register("emergency_button_press", EmergencyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, WaxOffTrigger> WAX_OFF_TRIGGER =
            TRIGGERS.register("wax_off", WaxOffTrigger::new);
}
