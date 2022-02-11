package draylar.crimsonmoon.item;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.api.event.CrimsonMoonEvents;
import draylar.crimsonmoon.data.CrimsonMoonData;
import draylar.worlddata.api.WorldData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScarletTearItem extends Item {

    public ScarletTearItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return ((TranslatableText) super.getName(stack)).formatted(Formatting.DARK_RED);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(LiteralText.EMPTY);
        tooltip.add(new LiteralText("Use to summon a Crimson Moon.").formatted(Formatting.GRAY));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient) {
            // no end/nether crimson moon
            if (!world.getRegistryKey().equals(World.END) && !world.getRegistryKey().equals(World.NETHER)) {

                // don't double-stack in a world that already has an active crimson moon
                CrimsonMoonData moonData = WorldData.getData((ServerWorld) world, CrimsonMoon.CRIMSON_MOON_ACTIVE);
                if (!moonData.isCrimsonMoon()) {
                    long trueDayTime = CrimsonMoon.getTrueDayTime(world);

                    // Check for valid Crimson Moon timing
                    if (trueDayTime >= 13000 && trueDayTime <= 23031) {
                        moonData.setCrimsonMoon(true);
                        CrimsonMoonEvents.START.invoker().run((ServerWorld) world);
                        ItemStack stackInHand = user.getStackInHand(hand);
                        stackInHand.decrement(1);
                        return TypedActionResult.success(stackInHand);
                    }
                }

            }
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        return super.use(world, user, hand);
    }
}
