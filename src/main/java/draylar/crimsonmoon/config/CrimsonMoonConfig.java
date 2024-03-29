package draylar.crimsonmoon.config;

import com.google.common.collect.Sets;
import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrimsonMoonConfig implements Config {

    @Comment(value = "Delay in seconds between each attempt to spawn a new mob in every loaded chunk during a crimson mood. Values less than 5 seconds may start to cause lag.")
    public int spawnDelaySeconds = 5;

    @Comment(value = "Maximum amount of mobs allowed in a chunk when spawning new ones during a crimson moon.")
    public int maxMobCountPerChunk = 3;

    @Comment(value = "Whether or not beds are disabled during a crimson moon.")
    public boolean disableBeds = true;

    @Comment(value = "Whether the red shader is enabled. Set this to false to disable the shader.")
    public boolean enableShader = true;

    @Comment(value = "Whether or not the moon should have a red-tinted texture during a crimson moon.")
    public boolean customMoonTexture = true;

    @Comment(value = "Whether or not rain should have a red-tinted texture during a crimson moon.")
    public boolean customRainTexture = true;

    @Comment(value = "The average number of days between each Crimson Moon. 20 is approximately one crimson moon every 20 days.")
    public int averageNightsBetweenCrimsonMoons = 20;

    @Comment(value = "Intensity of the red glow of the Crimson Moon. 0.4 is default, 1.0 is none.")
    public double glowIntensity = 0.4;

    @Comment(value = "If true, custom items are enabled. Make sure the values are the same on the client & server. Do not toggle this if you do not want to lose your custom items.")
    public boolean enableCustomItems = true;

    @Comment(value = "Base damage of the Carnage Weapon.")
    public int carnageDamage = 5;

    @Comment(value = "Maximum number of chunks (as a radius) around a player to spawn entities during a Crimson Moon.")
    public int maxChunkSpawnRadius = 8;

    @Comment(value = "A list of blocked mod IDs for Crimson Moon entity spawns.")
    public Set<String> blacklistModid = new HashSet<>(List.of("mythicmounts"));

    @Comment(value = "Blacklist of entity spawn IDs.")
    public Set<String> blacklistedEntityID = new HashSet<>(List.of("minecraft:slime"));

    @Override
    public String getName() {
        return "crimsonmoon";
    }

    @Override
    public String getExtension() {
        return "json5";
    }
}
