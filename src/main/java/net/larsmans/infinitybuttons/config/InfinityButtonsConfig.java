package net.larsmans.infinitybuttons.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class InfinityButtonsConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue MUTE_ALARM_SOUND = BUILDER
            .comment("Mute the emergency button alarm sound")
            .define("muteAlarmSound", false);

    public static final ModConfigSpec.EnumValue<AlarmEnum> ALARM_SOUND_TYPE = BUILDER
            .comment("How far the emergency alarm is heard")
            .defineEnum("alarmSoundType", AlarmEnum.RANGE);

    public static final ModConfigSpec.IntValue ALARM_SOUND_RANGE = BUILDER
            .comment("Chunk-like range used when alarmSoundType is RANGE")
            .defineInRange("alarmSoundRange", 6, 1, 32);

    public static final ModConfigSpec.BooleanValue ALARM_VILLAGER_PANIC = BUILDER
            .comment("Make villagers panic when an emergency button is pressed")
            .define("alarmVillagerPanic", true);

    public static final ModConfigSpec.BooleanValue TOOLTIPS = BUILDER
            .comment("Show extra item tooltips")
            .define("tooltips", true);

    public static final ModConfigSpec.BooleanValue DIAMOND_PARTICLES = BUILDER
            .comment("Spawn sparkle particles on diamond buttons")
            .define("diamondParticles", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
