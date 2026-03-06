package org.lightning323.frikinjay.letmedespawn;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class MobMixinUtils {
//Static methods need to be outside mixins!


    // ported from almanac
    public static void dropEquipmentOnPickup(ServerLevel server,  Mob entity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                // create a copy of their items and drop it
                ItemStack dropStack = itemStack.copy();
                entity.spawnAtLocation( server, dropStack);
            }
        }
    }

    // ported from almanac
    public static void dropEquipmentOnDiscard(ServerLevel server, Mob entity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {

            ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                entity.spawnAtLocation(server, itemStack);
                entity.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }
        }
    }
}
