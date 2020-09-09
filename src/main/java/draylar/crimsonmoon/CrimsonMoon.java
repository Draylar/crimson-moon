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
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class CrimsonMoon implements ModInitializer {

    public static final ComponentType<WorldCrimsonMoonComponent> CRIMSON_MOON_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(id("crimsonmoon"), WorldCrimsonMoonComponent.class);
    public static final CrimsonMoonConfig CONFIG = AutoConfig.register(CrimsonMoonConfig.class, JanksonConfigSerializer::new).getConfig();

    @Override
    public void onInitialize() {
        WorldComponentCallback.EVENT.register((world, components) -> components.put(CRIMSON_MOON_COMPONENT, new WorldCrimsonMoonComponent(world)));

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getWorlds().forEach(world -> {
                if (CRIMSON_MOON_COMPONENT.get(world).isCrimsonMoon() && world.random.nextInt(CONFIG.spawnDelaySeconds * 20) == 0) {
                    if (world.getTimeOfDay() >= 13188) {
                        WorldUtils.getLoadedChunks(world).forEach(chunk -> {
                            ChunkPos pos = chunk.getPos();
                            if (world.getEntitiesByClass(HostileEntity.class, new Box(pos.getStartPos(), pos.getStartPos().add(16, 256, 16)), e -> true).size() < CONFIG.maxMobCountPerChunk) {
                                int randomX = world.random.nextInt(16);
                                int randomZ = world.random.nextInt(16);
                                ChunkPos chunkPos = chunk.getPos();

                                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, chunkPos.getStartX() + randomX, chunkPos.getStartZ() + randomZ);
                                BlockPos spawnPos = new BlockPos(chunkPos.getStartX() + randomX, y, chunkPos.getStartZ() + randomZ);

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
        List<SpawnSettings.SpawnEntry> list = chunkGenerator.getEntitySpawnList(biome, accessor, spawnGroup, pos);
        return list.isEmpty() ? null : WeightedPicker.getRandom(random, list);
    }

    public static Identifier id(String name) {
        return new Identifier("crimsonmoon", name);
    }
}
