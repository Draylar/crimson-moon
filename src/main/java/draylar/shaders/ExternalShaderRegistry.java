package draylar.shaders;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ExternalShaderRegistry {

    public static final Map<ExternalShader, Boolean> TO_LOAD = new HashMap<>();

    public static ExternalShader load(Identifier id) {
        return ExternalShaderRegistry.load(id, true);
    }

    public static ExternalShader load(Identifier id, boolean loadFailure) {
        ExternalShader shader = new ExternalShader(id);
        TO_LOAD.put(shader, loadFailure);
        return shader;
    }
}
