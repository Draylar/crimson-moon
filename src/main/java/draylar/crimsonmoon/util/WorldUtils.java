package draylar.crimsonmoon.util;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {

    /**
     * Retrieves a list of all loaded Chunks in the given world.
     * Accomplished by iterating over all connected players and storing each chunk within the server render distance from them.
     * @param world world to retrieve loaded chunks from
     * @return a list of loaded chunks in the given world
     */
    public static List<WorldChunk> getLoadedChunks(ServerWorld world) {
        ArrayList<WorldChunk> loadedChunks = new ArrayList<>();
        int renderDistance = world.getServer().getPlayerManager().getViewDistance();

        world.getPlayers().forEach(player -> {
            ChunkPos playerChunkPos = new ChunkPos(player.getBlockPos());
            WorldChunk chunk = world.getChunk(playerChunkPos.x, playerChunkPos.z);

            if(!loadedChunks.contains(chunk)) {
                loadedChunks.add(chunk);
            }

            for(int x = -renderDistance; x <= renderDistance; x++) {
                for(int z = -renderDistance; z <= renderDistance; z++) {
                    ChunkPos offsetChunkPos = new ChunkPos(playerChunkPos.x + x, playerChunkPos.z + z);
                    WorldChunk offsetChunk = world.getChunk(offsetChunkPos.x, offsetChunkPos.z);

                    if(!loadedChunks.contains(offsetChunk)) {
                        loadedChunks.add(offsetChunk);
                    }
                }
            }
        });

        return loadedChunks;
    }
}
