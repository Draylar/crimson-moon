package draylar.shaders.mixin;


import com.google.gson.JsonParseException;
import draylar.shaders.ExternalShaderRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.Window;
import net.minecraft.resource.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Final private TextureManager textureManager;
    @Shadow @Final private ReloadableResourceManager resourceManager;
    @Shadow @Final private Framebuffer framebuffer;
    @Shadow public abstract Window getWindow();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void loadExternalShaders(RunArgs args, CallbackInfo ci) {
        ExternalShaderRegistry.TO_LOAD.forEach((shader, crashOnFail) -> {
            try {
                ShaderEffect effect = new ShaderEffect(textureManager, resourceManager, framebuffer, shader.getShaderLocation());
                effect.setupDimensions(getWindow().getFramebufferWidth(), getWindow().getFramebufferHeight());
                shader.setShaderEffect(effect);
            } catch (IOException | JsonParseException exception) {
                if(crashOnFail) {
                    throw new RuntimeException(exception);
                } else {
                    exception.printStackTrace();
                    shader.setShaderEffect(null);
                }
            }
        });
    }
}
