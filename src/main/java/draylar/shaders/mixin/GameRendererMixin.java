package draylar.shaders.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import draylar.shaders.ExternalShaderRegistry;
import draylar.shaders.api.ShaderRenderingEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V"))
    private void callExternalShaders(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.resetTextureMatrix();
        ShaderRenderingEvents.RENDER_SHADERS.invoker().render(tickDelta);
    }

    @Inject(method = "onResized", at = @At(value = "HEAD"))
    private void resizeShaders(int width, int height, CallbackInfo ci) {
        ExternalShaderRegistry.TO_LOAD.forEach((externalShader, loadFailure) -> {
            if(externalShader.getShader() != null) {
                externalShader.getShader().setupDimensions(width, height);
            }
        });
    }
}
