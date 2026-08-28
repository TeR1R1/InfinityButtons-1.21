package net.larsmans.infinitybuttons.item;

import net.larsmans.infinitybuttons.InfinityButtons;
import net.larsmans.infinitybuttons.block.InfinityButtonsBlocks;
import net.larsmans.infinitybuttons.block.custom.emergencybutton.SafeEmergencyButton;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InfinityButtonsItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(InfinityButtons.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, InfinityButtons.MOD_ID);

    // Register a BlockItem for every block, except the safe emergency buttons (custom item)
    // and the torch blocks (registered together with their wall variant below).
    // Block construction is deferred into the supplier, so holder.get() only resolves
    // once blocks have actually been registered (during the item registration event).
    static {
        java.util.Set<String> torchBlocks = java.util.Set.of(
                "torch_button", "wall_torch_button", "torch_lever", "wall_torch_lever",
                "soul_torch_button", "soul_wall_torch_button", "soul_torch_lever", "soul_wall_torch_lever",
                "redstone_torch_button", "redstone_wall_torch_button", "redstone_torch_lever", "redstone_wall_torch_lever");
        for (DeferredHolder<Block, ? extends Block> holder : InfinityButtonsBlocks.BLOCKS.getEntries()) {
            String name = holder.getId().getPath();
            if (torchBlocks.contains(name)) {
                continue;
            }
            ITEMS.register(name, () -> {
                Block block = holder.get();
                if (block instanceof SafeEmergencyButton) {
                    return new SafeEmergencyButtonItem(block, new Item.Properties());
                }
                return new BlockItem(block, new Item.Properties());
            });
        }
    }

    /**
     * Torches
     */

    public static final DeferredItem<Item> TORCH_BUTTON = registerTorch("torch_button", InfinityButtonsBlocks.TORCH_BUTTON, InfinityButtonsBlocks.WALL_TORCH_BUTTON);
    public static final DeferredItem<Item> TORCH_LEVER = registerTorch("torch_lever", InfinityButtonsBlocks.TORCH_LEVER, InfinityButtonsBlocks.WALL_TORCH_LEVER);
    public static final DeferredItem<Item> SOUL_TORCH_BUTTON = registerTorch("soul_torch_button", InfinityButtonsBlocks.SOUL_TORCH_BUTTON, InfinityButtonsBlocks.SOUL_WALL_TORCH_BUTTON);
    public static final DeferredItem<Item> SOUL_TORCH_LEVER = registerTorch("soul_torch_lever", InfinityButtonsBlocks.SOUL_TORCH_LEVER, InfinityButtonsBlocks.SOUL_WALL_TORCH_LEVER);
    public static final DeferredItem<Item> REDSTONE_TORCH_BUTTON = registerTorch("redstone_torch_button", InfinityButtonsBlocks.REDSTONE_TORCH_BUTTON, InfinityButtonsBlocks.REDSTONE_WALL_TORCH_BUTTON);
    public static final DeferredItem<Item> REDSTONE_TORCH_LEVER = registerTorch("redstone_torch_lever", InfinityButtonsBlocks.REDSTONE_TORCH_LEVER, InfinityButtonsBlocks.REDSTONE_WALL_TORCH_LEVER);

    private static DeferredItem<Item> registerTorch(String name, DeferredBlock<Block> standing, DeferredBlock<Block> wall) {
        return ITEMS.register(name, () -> new StandingAndWallBlockItem(standing.get(), wall.get(), new Item.Properties(), Direction.DOWN));
    }
}
