package net.larsmans.infinitybuttons;

import net.larsmans.infinitybuttons.advancement.InfinityButtonsTriggers;
import net.larsmans.infinitybuttons.block.InfinityButtonsBlocks;
import net.larsmans.infinitybuttons.config.InfinityButtonsConfig;
import net.larsmans.infinitybuttons.item.InfinityButtonsItemGroup;
import net.larsmans.infinitybuttons.item.InfinityButtonsItems;
import net.larsmans.infinitybuttons.item.SafeEmergencyButtonItem;
import net.larsmans.infinitybuttons.network.IBNetwork;
import net.larsmans.infinitybuttons.particle.InfinityButtonsParticleTypes;
import net.larsmans.infinitybuttons.sounds.InfinityButtonsSounds;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(InfinityButtons.MOD_ID)
public class InfinityButtons {
    public static final String MOD_ID = "infinitybuttons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public InfinityButtons(IEventBus modBus, ModContainer container) {
        InfinityButtonsBlocks.BLOCKS.register(modBus);
        InfinityButtonsItems.ITEMS.register(modBus);
        InfinityButtonsSounds.SOUNDS.register(modBus);
        InfinityButtonsParticleTypes.PARTICLES.register(modBus);
        InfinityButtonsTriggers.TRIGGERS.register(modBus);
        // Force the creative tab class to load so its tab entry is added to CREATIVE_TABS.
        InfinityButtonsItemGroup.INFINITYBUTTONS.getId();
        InfinityButtonsItems.CREATIVE_TABS.register(modBus);

        modBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(IBEvents.class);
        container.registerConfig(ModConfig.Type.COMMON, InfinityButtonsConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (DeferredHolder<net.minecraft.world.item.Item, ? extends net.minecraft.world.item.Item> holder : InfinityButtonsItems.ITEMS.getEntries()) {
                if (holder.get() instanceof SafeEmergencyButtonItem item) {
                    DispenserBlock.registerBehavior(item, new OptionalDispenseItemBehavior() {
                        @Override
                        protected ItemStack execute(BlockSource source, ItemStack stack) {
                            this.setSuccess(ArmorItem.dispenseArmor(source, stack));
                            return stack;
                        }
                    });
                }
            }
        });
    }
}
