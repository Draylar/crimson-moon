package draylar.crimsonmoon.data;

import draylar.crimsonmoon.network.ServerNetworking;
import draylar.worlddata.api.WorldData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class CrimsonMoonData implements WorldData {

    private static final String CRIMSON_MOON_KEY = "CrimsonMoon";
    private final ServerWorld world;
    private boolean isCrimsonMoon = false;

    public CrimsonMoonData(ServerWorld world) {
        this.world = world;
    }

    public boolean isCrimsonMoon() {
        return isCrimsonMoon;
    }

    public void setCrimsonMoon(boolean toggle) {
        this.isCrimsonMoon = toggle;

        // Notify all clients about the change.
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeBoolean(isCrimsonMoon);
        for (ServerPlayerEntity player : PlayerLookup.world(world)) {
            ServerPlayNetworking.send(player, ServerNetworking.CRIMSON_UPDATE, packet);
        }
    }

    public void update(ServerPlayerEntity player) {
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeBoolean(isCrimsonMoon);
        ServerPlayNetworking.send(player, ServerNetworking.CRIMSON_UPDATE, packet);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putBoolean(CRIMSON_MOON_KEY, isCrimsonMoon);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        this.isCrimsonMoon = tag.getBoolean(CRIMSON_MOON_KEY);
    }

    @Override
    public World getWorld() {
        return world;
    }
}
