package draylar.crimsonmoon.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.world.EntityList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityList.class)
public interface EntityListAccessor {
    @Accessor
    Int2ObjectMap<Entity> getEntities();
}
