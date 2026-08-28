package net.larsmans.infinitybuttons.item;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.larsmans.infinitybuttons.block.InfinityButtonsBlocks;
import net.larsmans.infinitybuttons.block.custom.DoorbellButton;
import net.larsmans.infinitybuttons.block.custom.LampButton;
import net.larsmans.infinitybuttons.block.custom.LanternButton;
import net.larsmans.infinitybuttons.block.custom.button.AbstractSmallButton;
import net.larsmans.infinitybuttons.block.custom.button.WoodenButton;
import net.larsmans.infinitybuttons.block.custom.consolebutton.ConsoleButton;
import net.larsmans.infinitybuttons.block.custom.emergencybutton.EmergencyButton;
import net.larsmans.infinitybuttons.block.custom.emergencybutton.SafeEmergencyButton;
import net.larsmans.infinitybuttons.block.custom.letterbutton.LetterButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.AbstractSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.BookshelfSecretButton;
import net.larsmans.infinitybuttons.block.custom.secretbutton.PlankSecretButton;
import net.larsmans.infinitybuttons.block.custom.torch.TorchButton;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class InfinityButtonsItemGroup {

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> INFINITYBUTTONS = InfinityButtonsItems.CREATIVE_TABS.register("infinitybuttons",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.infinitybuttons"))
                    .icon(() -> new ItemStack(InfinityButtonsBlocks.OAK_LARGE_BUTTON.get()))
                    .displayItems(InfinityButtonsItemGroup::buildContents)
                    .build());

    private static final List<String> BEFORE_PLANKS = List.of(
            "brick_secret_button", "stone_brick_secret_button", "mossy_stone_brick_secret_button",
            "cracked_stone_brick_secret_button", "chiseled_stone_brick_secret_button",
            "deepslate_brick_secret_button", "cracked_deepslate_brick_secret_button",
            "deepslate_tile_secret_button", "cracked_deepslate_tile_secret_button");

    private static void buildContents(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        List<Item> items = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (id != null && id.getNamespace().equals(InfinityButtons.MOD_ID)) {
                items.add(item);
            }
        }
        items.sort(Comparator.comparing(InfinityButtonsItemGroup::rank)
                .thenComparing(o -> {
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(o);
                    return id != null ? id.getPath() : "";
                }));
        for (Item item : items) {
            output.accept(item);
        }
    }

    private static String rank(Item item) {
        Block block = Block.byItem(item);
        if (block instanceof AbstractSmallButton smallButton) {
            if (smallButton.isLarge()) {
                return block instanceof WoodenButton ? "BA" : "BZ";
            }
            return block instanceof WoodenButton ? "AA" : "AZ";
        } else if (block instanceof EmergencyButton || block instanceof SafeEmergencyButton) {
            return "C";
        } else if (block instanceof AbstractSecretButton) {
            if (block instanceof BookshelfSecretButton) {
                return "DA";
            } else if (BEFORE_PLANKS.contains(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)).getPath())) {
                return "DB";
            } else if (block instanceof PlankSecretButton) {
                return "DC";
            } else {
                return "DZ";
            }
        } else if (block instanceof DoorbellButton || block instanceof LampButton || block instanceof LetterButton) {
            return "E";
        } else if (block instanceof LanternButton) {
            return "F";
        } else if (block instanceof ConsoleButton) {
            return "G";
        } else if (block instanceof TorchButton) {
            return "H";
        } else {
            return "ZZ";
        }
    }
}
