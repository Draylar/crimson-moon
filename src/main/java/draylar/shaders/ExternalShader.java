package draylar.shaders;

import draylar.crimsonmoon.mixin.ShaderEffectAccessor;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vector4f;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a non-vanilla post-processing shader effect which can be rendered as needed.
 *
 * <p>
 * An {@link ExternalShader} can be loaded through {@link ExternalShaderRegistry}.
 */
public class ExternalShader {

    private final Identifier shaderLocation;
    private final Map<String, Object> uniforms = new HashMap<>();
    private ShaderEffect shader = null;

    public ExternalShader(Identifier shaderLocation) {
        this.shaderLocation = shaderLocation;
    }

    public void setUniform(String name, Object value) {
        uniforms.put(name, value);
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        uniforms.put(name, new Vector4f(x, y, z, w));
    }

    public void clearUniforms() {
        uniforms.clear();
    }

    /**
     * @return the underlying shader effect loaded by this External Shader. May be null depending on the loading process of the client.
     */
    @Nullable
    public ShaderEffect getShader() {
        return shader;
    }

    public Identifier getShaderLocation() {
        return shaderLocation;
    }

    public void render(float tickDelta) {
        uniforms.forEach((string, object) -> {
            ((ShaderEffectAccessor) shader).getPasses().forEach(pass -> {

                // TODO: support more uniform types
                if(object instanceof Vector4f vector4f) {
                    pass.getProgram().getUniformByNameOrDummy(string).set(vector4f);
                }
            });
        });

        shader.render(tickDelta);
    }

    public void setShaderEffect(ShaderEffect shader) {
        this.shader = shader;
    }
}
