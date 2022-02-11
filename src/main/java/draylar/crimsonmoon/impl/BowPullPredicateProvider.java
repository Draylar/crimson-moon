package draylar.crimsonmoon.impl;

import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class BowPullPredicateProvider implements UnclampedModelPredicateProvider {

    @Override
    public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        if(entity == null) {
            return 0.0F;
        } else {
            return entity.getActiveItem() != stack ? 0.0F : (float) (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
        }
    }
}
