package org.lightning323.mixin.letMeDespawn;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.lightning323.frikinjay.almanac.Almanac;
import org.lightning323.frikinjay.letmedespawn.LetMeDespawn;
import org.lightning323.frikinjay.letmedespawn.MobMixinUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"setItemSlotAndDropWhenKilled"}
    )
    private void letmedespawn$setItemSlotAndDropWhenKilled(EquipmentSlot slot, ItemStack stack, CallbackInfo info) {
        Mob entity = (Mob) (Object) this;
        LetMeDespawn.setPersistence(entity, slot);
    }

    @Redirect(
            method = {"checkDespawn"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;discard()V"
            )
    )
    private void letmedespawn$yeetusCheckus(Mob instance) {
        Mob entity = (Mob) (Object) this;
        if(entity.level() instanceof ServerLevel server && Almanac.pickedItems) {
            MobMixinUtils.dropEquipmentOnDiscard(server, instance);
        }
        this.discard();
    }
}