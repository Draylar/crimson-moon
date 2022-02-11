package draylar.crimsonmoon.mixin;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ActiveTargetGoal.class)
public interface FollowTargetGoalAccessor {
    @Accessor
    void setTargetPredicate(TargetPredicate targetPredicate);

    @Accessor
    TargetPredicate getTargetPredicate();
}
