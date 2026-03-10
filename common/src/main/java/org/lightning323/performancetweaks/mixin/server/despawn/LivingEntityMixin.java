package org.lightning323.performancetweaks.mixin.server.despawn;

import org.lightning323.performancetweaks.optimizations.server.despawn.MobMixinUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.lightning323.performancetweaks.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin {

    // Hook into the remove method to catch mob removal as this is needed in newer versions & can prevent against lagfixers
    @Inject(
            method = {"remove"},
            at = @At("HEAD")
    )
    private void letmedespawn$onRemove(CallbackInfo info) {
        if (ConfigManager.INSTANCE.enableLetMeDespawn) {
            if ((Object) this instanceof Mob entity) {
                // check if they picked  up items
                if (ConfigManager.INSTANCE.letMeDespawn_dropTools) {
                    // drop equiptment items
                    MobMixinUtils.dropEquipmentOnPickup(entity);
                }
            }
        }
    }

}