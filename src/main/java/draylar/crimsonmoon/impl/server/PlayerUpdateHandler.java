package draylar.crimsonmoon.impl.server;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.worlddata.api.WorldData;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PlayerUpdateHandler implements ServerEntityWorldChangeEvents.AfterPlayerChange, ServerPlayConnectionEvents.Join {

    @Override
    public void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
        WorldData.getData(destination, CrimsonMoon.CRIMSON_MOON_ACTIVE).update(player);
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        WorldData.getData(handler.getPlayer().getWorld(), CrimsonMoon.CRIMSON_MOON_ACTIVE).update(handler.getPlayer());
    }
}
