package org.lightning323.performancetweaks.mixin.despawn;

import org.lightning323.performancetweaks.optimizations.despawn.Despawn;
import org.lightning323.performancetweaks.optimizations.despawn.MobMixinUtils;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.lightning323.performancetweaks.config.ConfigManager;
import org.lightning323.performancetweaks.optimizations.despawn.MobMixin_I;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class)
public abstract class MobMixin extends LivingEntity implements MobMixin_I {

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    private boolean persistenceRequired;

    public void setPersistenceRequired(boolean val) {
        this.persistenceRequired = val;
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"setItemSlotAndDropWhenKilled"}
    )
    private void letmedespawn$setItemSlotAndDropWhenKilled(EquipmentSlot slot, ItemStack stack, CallbackInfo info) {
        if (ConfigManager.INSTANCE.enableDespawningOptimization) {
            Mob entity = (Mob) (Object) this;
            Despawn.setPersistence(entity, slot);
        }
    }

    @Redirect(
            method = {"checkDespawn"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;discard()V"
            )
    )
    private void letmedespawn$yeetusCheckus(Mob instance) {
        if (ConfigManager.INSTANCE.enableDespawningOptimization) {
            if (ConfigManager.INSTANCE.letMeDespawn_dropTools) {
                MobMixinUtils.dropEquipmentOnDiscard(instance);
            }
            this.discard();
        }
    }
}