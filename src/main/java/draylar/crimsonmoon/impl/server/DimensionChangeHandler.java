package draylar.crimsonmoon.impl.server;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.worlddata.api.WorldData;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class DimensionChangeHandler implements ServerEntityWorldChangeEvents.AfterPlayerChange {

    @Override
    public void afterChangeWorld(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
        WorldData.getData(destination, CrimsonMoon.CRIMSON_MOON_ACTIVE).update(player);
    }
}
