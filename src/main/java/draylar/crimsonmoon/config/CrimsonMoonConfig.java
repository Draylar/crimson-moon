package draylar.crimsonmoon.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "crimsonmoon")
public class CrimsonMoonConfig implements ConfigData {

    @Comment(value = "Delay in seconds between each attempt to spawn a new mob in every loaded chunk during a crimson mood. Values less than 5 seconds may start to cause lag.")
    public int spawnDelaySeconds = 5;

    @Comment(value = "Maximum amount of mobs allowed in a chunk when spawning new ones during a crimson moon.")
    public int maxMobCountPerChunk = 5;

    @Comment(value = "Whether or not beds are disabled during a crimson moon.")
    public boolean disableBeds = true;

    @Comment(value = "Whether or not the moon should have a red-tinted texture during a crimson moon.")
    public boolean customMoonTexture = true;

    @Comment(value = "Whether or not rain should have a red-tinted texture during a crimson moon.")
    public boolean customRainTexture = true;

    @Comment(value = "Chance to spawn a crimson moon every night. 20 is approximately one crimson moon every 20 days.")
    public int crimsonMoonChance = 20;
}
