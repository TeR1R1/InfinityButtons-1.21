package net.larsmans.infinitybuttons;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.larsmans.infinitybuttons.block.InfinityButtonsBlocks;
import net.larsmans.infinitybuttons.block.custom.emergencybutton.SafeEmergencyButton;
import net.larsmans.infinitybuttons.block.custom.letterbutton.LetterButton;
import net.larsmans.infinitybuttons.config.InfinityButtonsConfig;
import net.larsmans.infinitybuttons.item.SafeEmergencyButtonItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class InfinityButtonsUtil {
    public static final MutableComponent HOLD_SHIFT_TEXT = Component.translatable("infinitybuttons.tooltip.hold_shift").withStyle(ChatFormatting.GRAY);
    public static final MutableComponent SAFE_EMERGENCY_BUTTON_ACTIONBAR_TEXT = Component.translatable("infinitybuttons.actionbar.closed_safety_button");

    public static List<SafeEmergencyButtonItem> SAFETY_BUTTONS;

    public static Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK;
    public static Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK;
    public static Supplier<BiMap<Block, Block>> WAX_ON_BY_BLOCK;
    public static Supplier<BiMap<Block, Block>> WAX_OFF_BY_BLOCK;
    public static Supplier<BiMap<Block, Block>> STICKY_ON_BY_BLOCK;
    public static Supplier<BiMap<Block, Block>> STICKY_OFF_BY_BLOCK;

    public static boolean crouchClickOverrides(Block block) {
        return block instanceof SafeEmergencyButton || block instanceof LetterButton;
    }

    public static void tooltip(List<Component> tooltip, String name) {
        if (InfinityButtonsConfig.TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("infinitybuttons.tooltip." + name).withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(HOLD_SHIFT_TEXT);
            }
        }
    }

    public static void tooltip(List<Component> tooltip, String name1, String name2) {
        if (InfinityButtonsConfig.TOOLTIPS.get()) {
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("infinitybuttons.tooltip." + name1).withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("infinitybuttons.tooltip." + name2).withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(HOLD_SHIFT_TEXT);
            }
        }
    }

    public static void buildNext() {
        if (NEXT_BY_BLOCK == null) {
            NEXT_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
                    .put(InfinityButtonsBlocks.COPPER_BUTTON.get(), InfinityButtonsBlocks.EXPOSED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.EXPOSED_COPPER_BUTTON.get(), InfinityButtonsBlocks.WEATHERED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.WEATHERED_COPPER_BUTTON.get(), InfinityButtonsBlocks.OXIDIZED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.EXPOSED_COPPER_LARGE_BUTTON.get())
                    .put(InfinityButtonsBlocks.EXPOSED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.WEATHERED_COPPER_LARGE_BUTTON.get())
                    .put(InfinityButtonsBlocks.WEATHERED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.OXIDIZED_COPPER_LARGE_BUTTON.get())
                    .build());
            PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());
        }
    }

    public static void buildWax() {
        if (WAX_ON_BY_BLOCK == null) {
            WAX_ON_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
                    .put(InfinityButtonsBlocks.COPPER_BUTTON.get(), InfinityButtonsBlocks.WAXED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.EXPOSED_COPPER_BUTTON.get(), InfinityButtonsBlocks.WAXED_EXPOSED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.WEATHERED_COPPER_BUTTON.get(), InfinityButtonsBlocks.WAXED_WEATHERED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.OXIDIZED_COPPER_BUTTON.get(), InfinityButtonsBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.WAXED_COPPER_LARGE_BUTTON.get())
                    .put(InfinityButtonsBlocks.EXPOSED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.WAXED_EXPOSED_COPPER_LARGE_BUTTON.get())
                    .put(InfinityButtonsBlocks.WEATHERED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.WAXED_WEATHERED_COPPER_LARGE_BUTTON.get())
                    .put(InfinityButtonsBlocks.OXIDIZED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.WAXED_OXIDIZED_COPPER_LARGE_BUTTON.get())
                    .build());
            WAX_OFF_BY_BLOCK = Suppliers.memoize(() -> WAX_ON_BY_BLOCK.get().inverse());
        }
    }

    public static void buildSticky() {
        if (STICKY_ON_BY_BLOCK == null) {
            STICKY_ON_BY_BLOCK = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
                    .put(InfinityButtonsBlocks.WAXED_COPPER_BUTTON.get(), InfinityButtonsBlocks.STICKY_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(), InfinityButtonsBlocks.STICKY_EXPOSED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(), InfinityButtonsBlocks.STICKY_WEATHERED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get(), InfinityButtonsBlocks.STICKY_OXIDIZED_COPPER_BUTTON.get())
                    .put(InfinityButtonsBlocks.WAXED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.STICKY_COPPER_LARGE_BUTTON.get())
                    .put(InfinityButtonsBlocks.WAXED_EXPOSED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.STICKY_EXPOSED_COPPER_LARGE_BUTTON.get())
                    .put(InfinityButtonsBlocks.WAXED_WEATHERED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.STICKY_WEATHERED_COPPER_LARGE_BUTTON.get())
                    .put(InfinityButtonsBlocks.WAXED_OXIDIZED_COPPER_LARGE_BUTTON.get(), InfinityButtonsBlocks.STICKY_OXIDIZED_COPPER_LARGE_BUTTON.get())
                    .build());
            STICKY_OFF_BY_BLOCK = Suppliers.memoize(() -> STICKY_ON_BY_BLOCK.get().inverse());
        }
    }

    public static void buildSafety() {
        if (SAFETY_BUTTONS == null) {
            SAFETY_BUTTONS = new ArrayList<>();
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof SafeEmergencyButtonItem safety) {
                    SAFETY_BUTTONS.add(safety);
                }
            }
        }
    }

    public static int checkChains(Level level, BlockPos pos) {
        int i = 0;
        while (level.getBlockState(pos.above(i + 1)).getBlock() instanceof ChainBlock) {
            i++;
        }
        return i;
    }
}
