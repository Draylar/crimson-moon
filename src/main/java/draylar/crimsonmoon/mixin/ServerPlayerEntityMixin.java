package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.worlddata.api.WorldData;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(
            method = "moveToWorld",
            at = @At("HEAD"))
    private void onChangeWorld(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        // Player is traveling to the Nether
        if(destination.getRegistryKey().equals(World.NETHER)) {
            WorldData.getGlobalData(destination.getServer(), CrimsonMoon.WORLD_PROGRESSION).setHasVisitedNether(true);
        }
    }
}
