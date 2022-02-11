package draylar.crimsonmoon.impl.client;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.CrimsonMoonClient;
import draylar.shaders.api.ShaderRenderingEvents;
import net.minecraft.client.MinecraftClient;

public class CrimsonShaderHandler implements ShaderRenderingEvents.RenderShader {

    @Override
    public void render(float delta) {
        if(MinecraftClient.getInstance().world != null) {
            if(CrimsonMoonClient.crimsonMoonPresent) {
                double glowIntensity = Math.max(0.0, Math.min(1.0, CrimsonMoon.CONFIG.glowIntensity)); // [0, 1]
                double remaining = 1 - glowIntensity;
                long timeOfDay = CrimsonMoon.getTrueDayTime(MinecraftClient.getInstance().world);

                if(CrimsonMoonClient.hasBanner) {
                    glowIntensity = glowIntensity + remaining * (1 - (CrimsonMoonClient.bannerTicks / 200f));
                } else if(timeOfDay >= CrimsonMoonClient.FADE_START && timeOfDay <= CrimsonMoonClient.END_TIME) {
                    glowIntensity = glowIntensity + remaining * ((timeOfDay - CrimsonMoonClient.FADE_START) / 200f);
                }

                CrimsonMoonClient.CRIMSON_SHADER.setUniform("ColorModulate", 1.0f, (float) glowIntensity, (float) glowIntensity, 1.0f);
                CrimsonMoonClient.CRIMSON_SHADER.render(delta);
            }
        }
    }
}
