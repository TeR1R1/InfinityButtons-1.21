package net.larsmans.infinitybuttons.item;

import net.larsmans.infinitybuttons.advancement.InfinityButtonsTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class SafeEmergencyButtonItem extends BlockItem {
    public SafeEmergencyButtonItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof ServerPlayer serverPlayer && serverPlayer.getItemBySlot(EquipmentSlot.HEAD).is(this)) {
            InfinityButtonsTriggers.SAFETY_TRIGGER.get().trigger(serverPlayer);
        }
    }
}
