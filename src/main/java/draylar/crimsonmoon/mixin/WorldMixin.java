package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.CrimsonMoon;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow public abstract World getWorld();

    @Inject(method = "setTimeOfDay", at = @At("HEAD"))
    private void setTime(long time, CallbackInfo ci) {
        if(!getWorld().isClient) {
            // end of day
            if (time % 13000 == 0) {
                // ~1/20 chance to start a blood moon
                if (getWorld().random.nextInt(CrimsonMoon.CONFIG.crimsonMoonChance) == 0) {
                    CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).setCrimsonMoon(true);
                    CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).sync();

                    // print message
                    getWorld().getPlayers().forEach(player -> {
                        player.sendMessage(new TranslatableText("crimsonmoon.rising").formatted(Formatting.DARK_RED));
                    });
                }
            }

            // morning time, end blood moon if it's active
            else if (time % 23031 == 0) {
                if(CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).isCrimsonMoon()) {
                    CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).setCrimsonMoon(false);
                    CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).sync();

                    // print message
                    getWorld().getPlayers().forEach(player -> {
                        player.sendMessage(new TranslatableText("crimsonmoon.ending").formatted(Formatting.DARK_RED));
                    });
                }
            }

            // make sure it's not day time with a bloodmoon for stuff like /time add, but don't log
            else if (time > 23031 || time < 13000) {
                if(CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).isCrimsonMoon()) {
                    CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).setCrimsonMoon(false);
                    CrimsonMoon.CRIMSON_MOON_COMPONENT.get(getWorld()).sync();
                }
            }
        }
    }
}
