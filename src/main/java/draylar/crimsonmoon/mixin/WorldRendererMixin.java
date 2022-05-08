package draylar.crimsonmoon.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.CrimsonMoonClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
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

    @Unique private static final Identifier CRIMSON_MOON_PHASES = CrimsonMoon.id("textures/environment/crimson_moon_phases.png");
    @Unique private static final Identifier CRIMSON_RAIN = CrimsonMoon.id("textures/environment/crimson_rain.png");
    @Shadow @Final private static Identifier MOON_PHASES;
    @Shadow @Final private static Identifier RAIN;

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 1))
    private void redirectMoonTexture(int i, Identifier identifier) {
        if(CrimsonMoonClient.crimsonMoonPresent && CrimsonMoon.CONFIG.customMoonTexture) {
            RenderSystem.setShaderTexture(0, CRIMSON_MOON_PHASES);
        } else {
            RenderSystem.setShaderTexture(0, MOON_PHASES);
        }
    }

    @Redirect(method = "renderWeather",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 0))
    private void redirectRainTexture(int i, Identifier identifier) {
        if(CrimsonMoonClient.crimsonMoonPresent && CrimsonMoon.CONFIG.customRainTexture) {
            RenderSystem.setShaderTexture(0, CRIMSON_RAIN);
        } else {
            RenderSystem.setShaderTexture(0, RAIN);
        }
    }
}
