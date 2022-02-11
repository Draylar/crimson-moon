package draylar.crimsonmoon.network;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.impl.server.AttackPacketHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ServerNetworking {

    public static final Identifier ATTACK_REQUEST = CrimsonMoon.id("attack");
    public static final Identifier CRIMSON_UPDATE = CrimsonMoon.id("crimson_update");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ATTACK_REQUEST, new AttackPacketHandler());
    }
}
