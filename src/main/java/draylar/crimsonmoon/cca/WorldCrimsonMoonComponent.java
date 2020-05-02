package draylar.crimsonmoon.cca;

import draylar.crimsonmoon.CrimsonMoon;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.sync.WorldSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public class WorldCrimsonMoonComponent implements WorldSyncedComponent {

    private static final String CRIMSON_MOON_KEY = "CrimsonMoon";
    private final World world;
    private boolean isCrimsonMoon = false;

    public WorldCrimsonMoonComponent(World world) {
        this.world = world;
    }

    public boolean isCrimsonMoon() {
        return isCrimsonMoon;
    }

    public void setCrimsonMoon(boolean b) {
        this.isCrimsonMoon = b;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.isCrimsonMoon = tag.getBoolean(CRIMSON_MOON_KEY);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putBoolean(CRIMSON_MOON_KEY, isCrimsonMoon);
        return tag;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return CrimsonMoon.CRIMSON_MOON_COMPONENT;
    }
}
