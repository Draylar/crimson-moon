package draylar.crimsonmoon;

import draylar.crimsonmoon.cca.WorldCrimsonMoonComponent;
import draylar.crimsonmoon.config.CrimsonMoonConfig;
import draylar.crimsonmoon.util.WorldUtils;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.WorldComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// General Notes:
//    world.getTimeOfDay() is a GENERAL total-tick counter. If you add 100,000 to the time, this method will return a large number.
//    world.getTime() is world.getGameTime() in mojmap. It doesn't seem to be related to day time or the time command. guessing it's like a world age counter
public class CrimsonMoon implements ModInitializer {

    public static final ComponentType<WorldCrimsonMoonComponent> CRIMSON_MOON_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(id("crimsonmoon"), WorldCrimsonMoonComponent.class);
    public static final CrimsonMoonConfig CONFIG = AutoConfig.register(CrimsonMoonConfig.class, JanksonConfigSerializer::new).getConfig();

    @Override
    public void onInitialize() {
        WorldComponentCallback.EVENT.register((world, components) -> components.put(CRIMSON_MOON_COMPONENT, new WorldCrimsonMoonComponent(world)));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getWorlds().forEach(world -> {

                // Only tick Crimson Moon logic in worlds where the event is active.
                // Additionally, there is a spawn delay to lower TPS impact.
                //    This defaults to 1 second in the config file.
                if (CRIMSON_MOON_COMPONENT.get(world).isCrimsonMoon() && world.random.nextInt(CONFIG.spawnDelaySeconds * 20) == 0) {

                    // Ensure mobs can spawn at this time.
                    // 13188 is the general time most mobs can spawn (12969 in rainy weather).
                    if (getTrueDayTime(world) >= 13188) {

                        // Calculate a list of loaded chunks by looking at the position of every player
                        // in the world, and then spawn mobs in each chunk.
                        WorldUtils.getLoadedChunks(world).forEach(chunk -> {
                            ChunkPos pos = chunk.getPos();

                            // Before spawning mobs inside a chunk, ensure it has room for more mobs.
                            if (world.getEntitiesByClass(HostileEntity.class, new Box(pos.getStartPos(), pos.getStartPos().add(16, 256, 16)), e -> true).size() < CONFIG.maxMobCountPerChunk) {
                                // Calculate a position to spawn our new mob at.
                                // Mobs always spawn on the surface at this point.
                                // TODO: do we want mobs to spawn underground?
                                int randomX = world.random.nextInt(16);
                                int randomZ = world.random.nextInt(16);
                                ChunkPos chunkPos = chunk.getPos();
                                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, chunkPos.getStartX() + randomX, chunkPos.getStartZ() + randomZ);
                                BlockPos spawnPos = new BlockPos(chunkPos.getStartX() + randomX, y, chunkPos.getStartZ() + randomZ);

                                // Pick a random monster to spawn based on the spawn conditions of the area.
                                SpawnSettings.SpawnEntry spawnEntry = pickRandomSpawnEntry(
                                        world.getChunkManager().getChunkGenerator(),
                                        SpawnGroup.MONSTER,
                                        world.getRandom(),
                                        spawnPos,
                                        world.getStructureAccessor(),
                                        world.getBiome(spawnPos)
                                );

                                if(spawnEntry != null) {
                                    Entity entity = spawnEntry.type.create(world);

                                    if(entity != null) {
                                        // Ensure this entity can spawn at the given position
                                        // TODO: light levels?
                                        if(entity instanceof HostileEntity) {
                                            if(!HostileEntity.canSpawnInDark((EntityType<? extends HostileEntity>) spawnEntry.type, world, SpawnReason.EVENT, spawnPos, world.random)) {
                                                return;
                                            }
                                        }

                                        // Some mobs, like Skeletons, have to be initialized for them to spawn with bows.
                                        if(entity instanceof MobEntity) {
                                            ((MobEntity) entity).initialize(world, world.getLocalDifficulty(spawnPos), SpawnReason.EVENT, null, null);
                                        }

                                        entity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                                        entity.updateTrackedPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                                        entity.updatePosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                                        world.spawnEntity(entity);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        });
    }

    private static SpawnSettings.SpawnEntry pickRandomSpawnEntry(ChunkGenerator chunkGenerator, SpawnGroup spawnGroup, Random random, BlockPos pos, StructureAccessor accessor, Biome biome) {
        List<SpawnSettings.SpawnEntry> list = new ArrayList<>(chunkGenerator.getEntitySpawnList(biome, accessor, spawnGroup, pos));
        list.removeIf(spawnEntry -> spawnEntry.type.equals(EntityType.SLIME));
        return list.isEmpty() ? null : WeightedPicker.getRandom(random, list);
    }

    public static Identifier id(String name) {
        return new Identifier("crimsonmoon", name);
    }

    public static long getTrueDayTime(World world) {
        return world.getTimeOfDay() % 24000;
    }
}
