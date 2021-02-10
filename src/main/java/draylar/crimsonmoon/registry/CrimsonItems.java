package draylar.crimsonmoon.registry;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.item.CarnageItem;
import draylar.crimsonmoon.item.CrimsonBrewItem;
import draylar.crimsonmoon.item.ScarletTearItem;
import draylar.crimsonmoon.material.CrimsonToolMaterial;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;

public class CrimsonItems {

    public static final Item CARNAGE = register("carnage", new CarnageItem(new Item.Settings().group(CrimsonMoon.GROUP)));
    public static final AxeItem CRIMSON_CRUSHER = register("crimson_crusher", new AxeItem(CrimsonToolMaterial.INSTANCE, 7, -3.1f, new Item.Settings().group(CrimsonMoon.GROUP)) {});
    public static final Item CRIMSON_BREW = register("crimson_brew", new CrimsonBrewItem(new Item.Settings().group(CrimsonMoon.GROUP).maxCount(8)));
    public static final Item BLOODTHIRSTY_BOW = register("bloodthirsty_bow", new BowItem(new Item.Settings().group(CrimsonMoon.GROUP)));
    public static final Item CRIMSON_BONE_BLADE = register("crimson_bone_blade", new SwordItem(CrimsonToolMaterial.INSTANCE, 6, -2.4f, new Item.Settings().group(CrimsonMoon.GROUP)));
    public static final Item RIB_DESTROYER = register("rib_destroyer", new AxeItem(CrimsonToolMaterial.INSTANCE, 6, -2.9f, new Item.Settings().group(CrimsonMoon.GROUP)) {});
    public static final Item SCARLET_TEAR = register("scarlet_tear", new ScarletTearItem(new Item.Settings().group(CrimsonMoon.GROUP)));
    public static final Item SCARLET_GEM = register("scarlet_gem", new Item(new Item.Settings().group(CrimsonMoon.GROUP)));

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, CrimsonMoon.id(name), item);
    }

    public static void init() {
        // NO-OP
    }

    private CrimsonItems() {
        // NO-OP
    }
}
