package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.CrimsonMoon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Unique
    private static final Identifier CRIMSON_MOON_PHASES = CrimsonMoon.id("textures/environment/crimson_moon_phases.png");

    @Unique
    private static final Identifier CRIMSON_RAIN = CrimsonMoon.id("textures/environment/crimson_rain.png");

    @Shadow @Final private TextureManager textureManager;

    @Shadow @Final private static Identifier MOON_PHASES;

    @Shadow private ClientWorld world;

    @Shadow @Final private static Identifier RAIN;

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V", ordinal = 1
            )
    )
    private void redirectMoonTexture(TextureManager textureManager, Identifier id) {
        if(CrimsonMoon.CRIMSON_MOON_COMPONENT.get(world).isCrimsonMoon() && CrimsonMoon.CONFIG.customMoonTexture) {
            this.textureManager.bindTexture(CRIMSON_MOON_PHASES);
        } else {
            this.textureManager.bindTexture(MOON_PHASES);
        }
    }

    @Redirect(
            method = "renderWeather",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V", ordinal = 0
            )
    )
    private void redirectRainTexture(TextureManager textureManager, Identifier id) {
        if(CrimsonMoon.CRIMSON_MOON_COMPONENT.get(world).isCrimsonMoon() && CrimsonMoon.CONFIG.customRainTexture) {
            this.textureManager.bindTexture(CRIMSON_RAIN);
        } else {
            this.textureManager.bindTexture(RAIN);
        }
    }
}
