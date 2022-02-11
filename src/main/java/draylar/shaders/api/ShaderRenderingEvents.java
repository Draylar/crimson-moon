package draylar.shaders.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ShaderRenderingEvents {

    public static final Event<RenderShader> RENDER_SHADERS = EventFactory.createArrayBacked(RenderShader.class, (RenderShader[] listeners) -> (delta) -> {
        for (RenderShader listener : listeners) {
            listener.render(delta);
        }
    });

    public interface RenderShader {
        void render(float delta);
    }
}
