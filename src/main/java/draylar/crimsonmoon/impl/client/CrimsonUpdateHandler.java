package draylar.crimsonmoon.impl.client;

import draylar.crimsonmoon.CrimsonMoonClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class CrimsonUpdateHandler implements ClientPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        boolean crimson = buf.readBoolean();
        client.execute(() -> {
            if(crimson) {
                CrimsonMoonClient.crimsonMoonPresent = true;
                CrimsonMoonClient.triggerBanner();
            } else {
                CrimsonMoonClient.crimsonMoonPresent = false;
            }
        });
    }
}
