package draylar.crimsonmoon;

import draylar.crimsonmoon.impl.*;
import draylar.crimsonmoon.impl.client.CrimsonShaderHandler;
import draylar.crimsonmoon.network.ClientNetworking;
import draylar.crimsonmoon.registry.CrimsonItems;
import draylar.shaders.ExternalShader;
import draylar.shaders.ExternalShaderRegistry;
import draylar.shaders.api.ShaderRenderingEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CrimsonMoonClient implements ClientModInitializer {

    public static final int END_TIME = 23031;
    public static final int FADE_START = END_TIME - 200;
    public static final Identifier BANNER_TEXTURE = CrimsonMoon.id("textures/gui/banner.png");
    public static final ExternalShader CRIMSON_SHADER = ExternalShaderRegistry.load(new Identifier("shaders/post/crimson.json"));
    public static boolean hasBanner = false;
    public static int bannerTicks = 0;

    public static boolean crimsonMoonPresent = false;

    @Override
    public void onInitializeClient() {
        ClientNetworking.init();
        ShaderRenderingEvents.RENDER_SHADERS.register(new CrimsonShaderHandler());
        ClientTickEvents.START_CLIENT_TICK.register(new BannerTickHandler());
        HudRenderCallback.EVENT.register(new BannerHudRenderer());

        // Functionality dependent on config options
        if(CrimsonMoon.CONFIG.enableCustomItems) {
            ClientTickEvents.START_CLIENT_TICK.register(new AttackKeyHandler());
            FabricModelPredicateProviderRegistry.register(CrimsonItems.BLOODTHIRSTY_BOW, new Identifier("pull"), new BowPullPredicateProvider());
            FabricModelPredicateProviderRegistry.register(CrimsonItems.BLOODTHIRSTY_BOW, new Identifier("pulling"), new BowPullingPredicateProvider());
        }
    }

    public static void triggerBanner() {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_TOAST_IN, 0.0F, 5.0F));
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.0F, 1.0F));
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ENTITY_ZOMBIE_AMBIENT, 0.5F, 0.75F));
        hasBanner = true;
        bannerTicks = 0;
    }
}
