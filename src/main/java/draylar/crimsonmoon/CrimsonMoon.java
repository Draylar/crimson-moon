package draylar.crimsonmoon;

import draylar.crimsonmoon.config.CrimsonMoonConfig;
import draylar.crimsonmoon.data.CrimsonMoonData;
import draylar.crimsonmoon.data.WorldProgressData;
import draylar.crimsonmoon.impl.server.DimensionChangeHandler;
import draylar.crimsonmoon.network.ServerNetworking;
import draylar.crimsonmoon.registry.CrimsonEventHandlers;
import draylar.crimsonmoon.registry.CrimsonItems;
import draylar.omegaconfig.OmegaConfig;
import draylar.worlddata.api.WorldDataKey;
import draylar.worlddata.api.WorldDataRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

// General Notes:
//    world.getTimeOfDay() is a GENERAL total-tick counter. If you add 100,000 to the time, this method will return a large number.
//    world.getTime() is world.getGameTime() in mojmap. It doesn't seem to be related to day time or the time command. guessing it's like a world age counter
public class CrimsonMoon implements ModInitializer {

    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(id("group"), () -> new ItemStack(CrimsonItems.CARNAGE)).setTexture("crimson.png");
    public static final WorldDataKey<CrimsonMoonData> CRIMSON_MOON_ACTIVE = WorldDataRegistry.register(id("crimsonmoon"), CrimsonMoonData::new);
    public static final WorldDataKey<WorldProgressData> WORLD_PROGRESSION = WorldDataRegistry.registerGlobal(id("progress"), WorldProgressData::new);
    public static final CrimsonMoonConfig CONFIG = OmegaConfig.register(CrimsonMoonConfig.class);

    @Override
    public void onInitialize() {
        CrimsonItems.init();
        ServerNetworking.init();
        CrimsonEventHandlers.register();
        BrewingRecipeRegistry.ITEM_RECIPES.add(new BrewingRecipeRegistry.Recipe<>(Items.POTION, Ingredient.ofItems(CrimsonItems.SCARLET_GEM), CrimsonItems.CRIMSON_BREW));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(new DimensionChangeHandler());
    }

    @Nullable
    public static SpawnSettings.SpawnEntry pickRandomSpawnEntry(ChunkGenerator chunkGenerator, SpawnGroup spawnGroup, Random random, BlockPos pos, StructureAccessor accessor, Biome biome) {
        Pool<SpawnSettings.SpawnEntry> pool = chunkGenerator.getEntitySpawnList(biome, accessor, spawnGroup, pos);
//        pool.removeIf(spawnEntry -> spawnEntry.type.equals(EntityType.SLIME));
        return pool.getOrEmpty(random).orElse(null);
    }

    public static Identifier id(String name) {
        return new Identifier("crimsonmoon", name);
    }

    public static long getTrueDayTime(World world) {
        return world.getTimeOfDay() % 24000;
    }

}
