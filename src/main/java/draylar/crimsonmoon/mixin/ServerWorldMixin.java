package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.CrimsonMoon;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

    private ServerWorldMixin(MutableWorldProperties mutableWorldProperties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, Supplier<Profiler> profiler, boolean bl, boolean bl2, long l) {
        super(mutableWorldProperties, registryKey, registryKey2, dimensionType, profiler, bl, bl2, l);
    }

    @Inject(
            method = "setTimeOfDay",
            at = @At(value = "HEAD")
    )
    private void setTime(long timeOfDay, CallbackInfo ci) {
        long cappedDayTime = timeOfDay % 24000;

        // only start blood moon if daylight cycle is on
        if(this.properties.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            // end of day
            if (cappedDayTime % 13000 == 0) {
                // ~1/20 chance to start a blood moon
                if (getWorld().random.nextInt(CrimsonMoon.CONFIG.crimsonMoonChance) == 0) {
                    CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).setCrimsonMoon(true);
                    CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).sync();

                    // print message
                    getWorld().getPlayers().forEach(player -> {
                        player.sendMessage(new TranslatableText("crimsonmoon.rising").formatted(Formatting.DARK_RED), false);
                    });
                }
            }
        }

        // morning time, end blood moon if it's active
        if (cappedDayTime % 23031 == 0) {
            if (CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).isCrimsonMoon()) {
                CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).setCrimsonMoon(false);
                CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).sync();

                // print message
                getWorld().getPlayers().forEach(player -> {
                    player.sendMessage(new TranslatableText("crimsonmoon.ending").formatted(Formatting.DARK_RED), false);
                });
            }
        }

        // make sure it's not day time with a bloodmoon for stuff like /time add, but don't log
        else if (cappedDayTime > 23031 || cappedDayTime < 13000) {
            if (CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).isCrimsonMoon()) {
                CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).setCrimsonMoon(false);
                CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).sync();
            }
        }
    }
}
