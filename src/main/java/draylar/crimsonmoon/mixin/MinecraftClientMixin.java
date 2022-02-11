package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.api.ClientAttackUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    public ClientPlayerEntity player;

    @Inject(
            method = "doAttack",
            at = @At("HEAD")
    )
    private void onAttack(CallbackInfo ci) {
        ClientAttackUtils.requestAttack(player);
    }
}
