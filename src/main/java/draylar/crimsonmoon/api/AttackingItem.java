package draylar.crimsonmoon.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Allows an {@link net.minecraft.item.Item} to provide behavior to run when the client player "left-clicks" with the Item.
 */
public interface AttackingItem {

    /**
     * Called when a player holding an {@link ItemStack} containing this item left-clicks.
     *
     * @param player player that is left-clicking
     * @param world world the action is occurring in
     * @param stack stack that was left-clicked with
     */
    void attack(PlayerEntity player, World world, ItemStack stack);
}
