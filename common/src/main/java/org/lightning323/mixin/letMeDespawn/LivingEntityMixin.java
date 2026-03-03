package org.lightning323.mixin.letMeDespawn;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.lightning323.frikinjay.almanac.Almanac;
import org.lightning323.frikinjay.letmedespawn.MobMixinUtils;
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
        if ((Object) this instanceof Mob entity) {
            // check if they picked  up items
            if (Almanac.pickedItems) {
                // drop equiptment items
                MobMixinUtils.dropEquipmentOnPickup(entity);
            }
        }
    }

}