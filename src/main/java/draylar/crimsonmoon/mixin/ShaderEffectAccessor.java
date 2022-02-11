package draylar.crimsonmoon.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.PostProcessShader;
import net.minecraft.client.gl.ShaderEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ShaderEffect.class)
public interface ShaderEffectAccessor {
    @Accessor
    List<PostProcessShader> getPasses();
}
