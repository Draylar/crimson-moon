package draylar.crimsonmoon.api;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.mixin.EnderDragonFightAccessor;
import draylar.worlddata.api.WorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public enum ServerPhase {
    START(.2f, 0, 1, 1),
    NETHER(.5f, 1, 3, 2),
    POST_DRAGON(.75f, 1, 4, 3);

    private final float chance;
    private final int level;
    private final int maxLevel;
    private final float trackingRangeMultiplier;

    ServerPhase(float chance, int level, int maxLevel, float trackingRangeMultiplier) {
        this.chance = chance;
        this.level = level;
        this.maxLevel = maxLevel;
        this.trackingRangeMultiplier = trackingRangeMultiplier;
    }

    public float getChance() {
        return chance;
    }

    public int getLevel() {
        return level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public float getTrackingRangeMultiplier() {
        return trackingRangeMultiplier;
    }

    public static ServerPhase calculate(MinecraftServer server) {
        ServerWorld end = server.getWorld(World.END);

        // If the Dragon has been killed, return POST_DRAGON.
        if(end != null) {
            boolean hasKilledDragon = end.getEnderDragonFight() != null && (((EnderDragonFightAccessor) end.getEnderDragonFight()).getExitPortalLocation() != null && end.getEnderDragonFight().hasPreviouslyKilled());

            if(hasKilledDragon) {
                return POST_DRAGON;
            }
        }

        // If any player has visited the Nether, return NETHER.
        if(WorldData.getGlobalData(server, CrimsonMoon.WORLD_PROGRESSION).hasVisitedNether()) {
            return NETHER;
        }

        return START;
    }
}
