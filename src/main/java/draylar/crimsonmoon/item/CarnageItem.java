package draylar.crimsonmoon.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import draylar.crimsonmoon.api.AttackingItem;
import draylar.crimsonmoon.material.CrimsonToolMaterial;
import draylar.crimsonmoon.mixin.LivingEntityAccessor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CarnageItem extends SwordItem implements AttackingItem {

    public CarnageItem(Settings settings) {
        super(CrimsonToolMaterial.INSTANCE, 0, -2.4f, settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return ((TranslatableText) super.getName(stack)).formatted(Formatting.DARK_RED);
    }

    @Override
    public void attack(PlayerEntity player, World world, ItemStack stack) {
        if(!world.isClient) {
            Vec3d rotationVector = player.getRotationVector();
            Vec3d pos = player.getPos().add(rotationVector.multiply(2)).add(0, ((LivingEntityAccessor) player).cm_getEyeHeight(player.getPose(), player.getDimensions(player.getPose())), 0);

            AtomicBoolean hit = new AtomicBoolean();
            world.getEntitiesByClass(LivingEntity.class, new Box(pos.x - 1, pos.y - .75, pos.z - 1, pos.x + 1, pos.y + .5, pos.z + 1), entity -> !entity.equals(player)).forEach(entity -> {
                entity.damage(DamageSource.player(player), EnchantmentHelper.getAttackDamage(stack, entity.getGroup()) + 5);

                // If the enemy being attacked by the Carnage was killed,
                // heal the player for a single heart.
                if(entity.isDead()) {
                    player.heal(2f);
                }

                hit.set(true);
            });

            // damage tool if any mobs were hit
            if(hit.get()) {
                stack.damage(1, player, ((p) -> p.sendToolBreakStatus(player.getActiveHand())));
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(new LiteralText("Crush my foes!").setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.GRAY)));
        tooltip.add(LiteralText.EMPTY);
        tooltip.add(new LiteralText("When in Main Hand: ").formatted(Formatting.GRAY));
        tooltip.add(new LiteralText(" 5 Attack Damage").formatted(Formatting.DARK_GREEN));
        tooltip.add(new LiteralText(" Auto-swing").formatted(Formatting.DARK_GREEN));
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return ImmutableMultimap.of();
    }
}
