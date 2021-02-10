package draylar.crimsonmoon.material;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class CrimsonToolMaterial implements ToolMaterial {

    public static final CrimsonToolMaterial INSTANCE = new CrimsonToolMaterial();

    @Override
    public int getDurability() {
        return 750;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 3;
    }

    @Override
    public float getAttackDamage() {
        return 0;
    }

    @Override
    public int getMiningLevel() {
        return 2;
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems();
    }
}
