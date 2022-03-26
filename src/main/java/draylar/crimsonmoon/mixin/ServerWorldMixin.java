package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.api.event.CrimsonMoonEvents;
import draylar.crimsonmoon.data.CrimsonMoonData;
import draylar.worlddata.api.WorldData;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

    private ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
    }

    @Inject(
            method = "setTimeOfDay",
            at = @At(value = "HEAD")
    )
    private void setTime(long timeOfDay, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        CrimsonMoonData data = WorldData.getData(world, CrimsonMoon.CRIMSON_MOON_ACTIVE);
        long cappedDayTime = timeOfDay % 24000;

        // For now, End/Nether dimensions do NOT have Crimson Moons.
        if(world.getRegistryKey().equals(World.END) || world.getRegistryKey().equals(World.NETHER)) {
            return;
        }

        // The Crimson Moon should only trigger if the daylight cycle gamerule has been enabled.
        if(this.properties.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {

            // It is the end of the day (13000 time).
            // We exclude 0 because it returns true for modulus, which resulted in a weird bug.
            if (cappedDayTime != 0 && cappedDayTime % 13000 == 0) {

                ActionResult test = CrimsonMoonEvents.BEFORE_START.invoker().test(world);
                if (test != ActionResult.FAIL) {
                    data.setCrimsonMoon(true);
                    CrimsonMoonEvents.START.invoker().run(world);
                }
            }
        }

        // Morning time! Finish the Crimson Moon if it is active.
        if (cappedDayTime != 0 && cappedDayTime % 23031 == 0) {
            if (data.isCrimsonMoon()) {
                data.setCrimsonMoon(false);
                CrimsonMoonEvents.END.invoker().run(world, false);
            }
        }

        // Ensure it is not day time with a Crimson Moon for cases like /time add, but don't log
        else if (cappedDayTime > 23031 || cappedDayTime < 13000) {
            if (data.isCrimsonMoon()) {
                data.setCrimsonMoon(false);
                CrimsonMoonEvents.END.invoker().run(world, true);
            }
        }
    }
}
