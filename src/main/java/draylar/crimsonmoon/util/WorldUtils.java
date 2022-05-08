package draylar.crimsonmoon.util;

import draylar.crimsonmoon.CrimsonMoon;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashSet;
import java.util.Set;

public class WorldUtils {

    /**
     * Retrieves a list of all loaded Chunks in the given world.
     * Accomplished by iterating over all connected players and storing each chunk within the server render distance from them.
     * @param world world to retrieve loaded chunks from
     * @return a list of loaded chunks in the given world
     */
    public static Set<WorldChunk> getLoadedChunks(ServerWorld world) {
        world.getProfiler().push("crimsonmoon_locatechunks");
        Set<WorldChunk> loadedChunks = new HashSet<>();
        Set<ChunkPos> occupiedChunks = new HashSet<>();

        int renderDistance = world.getServer().getPlayerManager().getViewDistance();

        world.getPlayers().forEach(player -> {
            ChunkPos playerChunkPos = new ChunkPos(player.getBlockPos());

            // Keep track of the chunks each player is standing in.
            // Mobs should not spawn directly next to players.
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    occupiedChunks.add(new ChunkPos(playerChunkPos.getStartX() + x, playerChunkPos.getStartZ() + z));
                }
            }

            // Iterate over nearby chunks.
            // We spawn mobs in all chunks within the render distance, halved, to save performance.
            int spawnRadius = renderDistance / 2;
            spawnRadius = Math.min(CrimsonMoon.CONFIG.maxChunkSpawnRadius, spawnRadius);
            for(int x = -spawnRadius; x <= spawnRadius; x++) {
                for(int z = -spawnRadius; z <= spawnRadius; z++) {
                    ChunkPos offsetChunkPos = new ChunkPos(playerChunkPos.x + x, playerChunkPos.z + z);
                    WorldChunk offsetChunk = world.getChunk(offsetChunkPos.x, offsetChunkPos.z);

                    // Only return unoccupied chunks (no players within a radius of 1 nearby).
                    if(!occupiedChunks.contains(offsetChunkPos)) {
                        loadedChunks.add(offsetChunk);
                    }
                }
            }
        });

        world.getProfiler().pop();
        return loadedChunks;
    }
}
