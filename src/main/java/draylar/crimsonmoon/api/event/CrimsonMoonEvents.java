package draylar.crimsonmoon.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Provides events related to the Crimson Moon.
 */
public class CrimsonMoonEvents {

    /**
     * Triggered when a Crimson Moon attempts to spawn.
     *
     * <p> The Crimson Moon always starts at 13000 game time, which is when hostile mobs start spawning.
     * If all handlers return {@link ActionResult#PASS} or one handler returns {@link ActionResult#SUCCESS}, the Crimson Moon will spawn.
     *
     * @see StartHandler for providing event handlers
     */
    public static Event<BeforeStartHandler> BEFORE_START = EventFactory.createArrayBacked(BeforeStartHandler.class, callbacks -> world -> {
        for(BeforeStartHandler start : callbacks) {
            ActionResult test = start.test(world);

            if(test != ActionResult.PASS) {
                return test;
            }
        }

        return ActionResult.PASS;
    });

    /**
     * Triggered when a Crimson Moon starts.
     *
     * <p> The Crimson Moon always starts at 13000 game time, which is when hostile mobs start spawning.
     *
     * @see StartHandler for providing event handlers
     */
    public static Event<StartHandler> START = EventFactory.createArrayBacked(StartHandler.class, callbacks -> world -> {
        for(StartHandler start : callbacks) {
            start.run(world);
        }
    });

    /**
     * Triggered when a Crimson Moon finishes (at 23031 day time).
     *
     * <p> The Crimson Moon typically stops at 23031 day time, which is when hostile stop spawning.
     * The Crimson Moon can also be forcibly stopped; this typically happens when players use the /time command.
     * If this is the case, the 'forced' flag will be true.
     *
     * @see EndHandler for providing event handlers
     */
    public static Event<EndHandler> END = EventFactory.createArrayBacked(EndHandler.class, callbacks -> (world, forced) -> {
        for(EndHandler start : callbacks) {
            start.run(world, forced);
        }
    });

    @FunctionalInterface
    public interface BeforeStartHandler {
        ActionResult test(ServerWorld world);
    }

    @FunctionalInterface
    public interface StartHandler {
        void run(ServerWorld world);
    }

    @FunctionalInterface
    public interface EndHandler {
        void run(ServerWorld world, boolean forced);
    }
}
