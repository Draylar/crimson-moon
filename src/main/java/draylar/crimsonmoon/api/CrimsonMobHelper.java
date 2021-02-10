package draylar.crimsonmoon.api;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CrimsonMobHelper {

    public static final HashMap<Integer, List<Item>> WEAPONS = new HashMap<>();

    static {
        WEAPONS.put(0, Arrays.asList(Items.WOODEN_AXE, Items.WOODEN_SWORD, Items.STONE_SHOVEL));
        WEAPONS.put(1, Arrays.asList(Items.STONE_SWORD, Items.STONE_AXE, Items.IRON_SHOVEL));
        WEAPONS.put(2, Arrays.asList(Items.GOLDEN_SWORD, Items.GOLDEN_AXE));
        WEAPONS.put(3, Arrays.asList(Items.IRON_SWORD, Items.IRON_AXE));
        WEAPONS.put(4, Arrays.asList(Items.DIAMOND_SWORD, Items.DIAMOND_AXE));
    }

    public static void initialize(MinecraftServer server, MobEntity entity) {
        ServerPhase phase = ServerPhase.calculate(server);
        World world = entity.world;
        Random random = world.random;

        // Initialize armor for mob
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if(random.nextFloat() <= phase.getChance()) {
                Item item = MobEntity.getEquipmentForSlot(slot, phase.getLevel() + random.nextInt(phase.getMaxLevel() - phase.getLevel() + 1)); // i should be [0, 4]

                if (item != null) {
                    entity.equipStack(slot, new ItemStack(item));
                }

                // Crimson Moon mobs don't drop armor/weapon loot
                entity.setEquipmentDropChance(slot, 0);
            }
        }

        // main-hand item
        if(random.nextFloat() <= phase.getChance()) {
            List<Item> items = WEAPONS.get(phase.getLevel() + random.nextInt(phase.getMaxLevel() - phase.getLevel() + 1));
            entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(items.get(random.nextInt(items.size()))));
        }

        // enchantments
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = entity.getEquippedStack(slot);

            if (!itemStack.isEmpty() && random.nextFloat() < 0.5F * phase.getLevel() / 2) {
                entity.equipStack(slot, EnchantmentHelper.enchant(random, itemStack, (int)(5.0F + (phase.getLevel() / 2) * (float) random.nextInt(18)), false));
            }
        }

        // attributes
        EntityAttributeInstance attributeInstance = entity.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE);
        if(attributeInstance != null) {
            attributeInstance.addPersistentModifier(new EntityAttributeModifier("Crimson Moon follow range", phase.getTrackingRangeMultiplier(), EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
}
