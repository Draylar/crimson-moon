package draylar.crimsonmoon.config;

import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;

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

    @Override
    public String getName() {
        return "crimsonmoon";
    }

    @Override
    public String getExtension() {
        return "json5";
    }
}
