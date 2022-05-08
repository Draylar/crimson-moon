package draylar.crimsonmoon.impl;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import static draylar.crimsonmoon.CrimsonMoonClient.bannerTicks;
import static draylar.crimsonmoon.CrimsonMoonClient.hasBanner;

public class BannerTickHandler implements ClientTickEvents.StartTick {

    @Override
    public void onStartTick(MinecraftClient client) {
        if(hasBanner) {
            bannerTicks++;

            if(bannerTicks >= 200) {
                bannerTicks = 0;
                hasBanner = false;
            }
        } else {
            bannerTicks = 0;
        }
    }
}
