package draylar.crimsonmoon.network;

import draylar.crimsonmoon.impl.client.CrimsonUpdateHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworking {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ServerNetworking.CRIMSON_UPDATE, new CrimsonUpdateHandler());
    }
}
