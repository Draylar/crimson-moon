package draylar.crimsonmoon.data;

import draylar.worlddata.api.WorldData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class WorldProgressData implements WorldData {

    private static final String VISITED_NETHER_KEY = "VisitedNether";
    private final ServerWorld world;
    private boolean hasVisitedNether = false;

    public WorldProgressData(ServerWorld world) {
        this.world = world;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        this.hasVisitedNether = tag.getBoolean(VISITED_NETHER_KEY);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putBoolean(VISITED_NETHER_KEY, hasVisitedNether);
    }

    public boolean hasVisitedNether() {
        return hasVisitedNether;
    }

    public void setHasVisitedNether(boolean hasVisitedNether) {
        this.hasVisitedNether = hasVisitedNether;
    }

    @Override
    public World getWorld() {
        return world;
    }
}
