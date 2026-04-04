package org.lightning323.performancetweaks.optimizations.despawn;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class MobMixinUtils {
//Static methods need to be outside mixins!


    // ported from almanac
    public static void dropEquipmentOnPickup(Mob entity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                // create a copy of their items and drop it
                ItemStack dropStack = itemStack.copy();
                entity.spawnAtLocation(dropStack);
            }
        }
    }

    // ported from almanac
    public static void dropEquipmentOnDiscard(Mob entity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                entity.spawnAtLocation(itemStack);
                entity.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }
        }
    }
}
