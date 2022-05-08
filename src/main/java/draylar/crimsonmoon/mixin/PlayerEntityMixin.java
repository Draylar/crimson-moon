package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.registry.CrimsonItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    public PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "attack",
            at = @At("HEAD"),
            cancellable = true)
    private void attemptAttack(Entity target, CallbackInfo ci) {
        if(CrimsonMoon.CONFIG.enableCustomItems && getMainHandStack().getItem().equals(CrimsonItems.CARNAGE)) {
            ci.cancel();
        }
    }
}
