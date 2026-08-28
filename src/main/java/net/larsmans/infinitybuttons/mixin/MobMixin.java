package net.larsmans.infinitybuttons.mixin;

import net.larsmans.infinitybuttons.InfinityButtonsUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.larsmans.infinitybuttons.InfinityButtonsUtil.SAFETY_BUTTONS;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    private void infinitybuttons$populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        InfinityButtonsUtil.buildSafety();
        if (random.nextDouble() < 0.001) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(SAFETY_BUTTONS.get(random.nextInt(SAFETY_BUTTONS.size()))));
            ((Mob)(Object)this).setDropChance(EquipmentSlot.HEAD, 1f);
            ci.cancel();
        }
    }
}
