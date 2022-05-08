package draylar.crimsonmoon.impl.client;

import draylar.crimsonmoon.CrimsonMoonClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class CrimsonStatusTickHandler implements ClientTickEvents.StartTick {

    @Override
    public void onStartTick(MinecraftClient client) {
        if(client.world == null) {
            CrimsonMoonClient.crimsonMoonPresent = false;
            CrimsonMoonClient.hasBanner = false;
            CrimsonMoonClient.bannerTicks = 0;
        }
    }
}
