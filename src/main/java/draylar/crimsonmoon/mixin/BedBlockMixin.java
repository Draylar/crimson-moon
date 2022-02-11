package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.worlddata.api.WorldData;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {

    @Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;"), cancellable = true)
    private void onTrySleep(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if(!world.isClient && WorldData.getData((ServerWorld) world, CrimsonMoon.CRIMSON_MOON_ACTIVE).isCrimsonMoon() && CrimsonMoon.CONFIG.disableBeds) {
            cir.setReturnValue(ActionResult.FAIL);
            player.sendMessage(new TranslatableText("crimsonmoon.failed_sleep_" + world.random.nextInt(3)).formatted(Formatting.DARK_RED), true);
        }
    }
}
