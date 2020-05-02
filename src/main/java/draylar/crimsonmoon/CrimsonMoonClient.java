package draylar.crimsonmoon;

import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CrimsonMoonClient implements ClientModInitializer {

    public static final ManagedShaderEffect CRIMSON_SHADER = ShaderEffectManager.getInstance()
            .manage(new Identifier("crimsonmoon", "shaders/post/crimson.json"));

    @Override
    public void onInitializeClient() {
        ShaderEffectRenderCallback.EVENT.register((v) -> {
            if(MinecraftClient.getInstance().world != null) {
                if (CrimsonMoon.CRIMSON_MOON_COMPONENT.get(MinecraftClient.getInstance().world).isCrimsonMoon()) {
                    CRIMSON_SHADER.render(v);
                }
            }
        });
    }
}
