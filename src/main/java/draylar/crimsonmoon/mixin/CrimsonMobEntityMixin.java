package draylar.crimsonmoon.mixin;

import draylar.crimsonmoon.api.Crimson;
import draylar.crimsonmoon.registry.CrimsonItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class CrimsonMobEntityMixin extends LivingEntity implements Crimson {

    private boolean cm_isCrimson = false;

    protected CrimsonMobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("RETURN"))
    private void onWriteData(NbtCompound tag, CallbackInfo ci) {
        tag.putBoolean("IsCrimson", cm_isCrimson);
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("RETURN"))
    private void onReadData(NbtCompound tag, CallbackInfo ci) {
        cm_isCrimson = tag.getBoolean("IsCrimson");
    }

    @Override
    public boolean cm_isCrimson() {
        return cm_isCrimson;
    }

    @Override
    public void cm_setCrimson(boolean crimson) {
        cm_isCrimson = crimson;
    }

    @Inject(
            method = "dropLoot",
            at = @At("RETURN"))
    private void onDropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if(!world.isClient && source.getSource() instanceof PlayerEntity) {
            if(cm_isCrimson) {
                if(world.random.nextInt(25) == 0) {
                    ItemScatterer.spawn(world, getX(), getY(), getZ(), new ItemStack(CrimsonItems.SCARLET_GEM));
                }
            }
        }
    }
}
