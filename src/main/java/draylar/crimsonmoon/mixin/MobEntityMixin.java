package draylar.crimsonmoon.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobEntity.class)
public class MobEntityMixin {

//    @Redirect(
//            method = "initEquipment",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/LocalDifficulty;getClampedLocalDifficulty()F")
//    )
//    private float redirect(LocalDifficulty localDifficulty) {
//        return 100;
//    }
}
