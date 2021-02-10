package draylar.crimsonmoon.network;

import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.api.AttackingItem;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ServerNetworking {

    public static final Identifier ATTACK_REQUEST = CrimsonMoon.id("attack");

    public static void init() {
        registerAttackPacketHandler();;
    }

    private static void registerAttackPacketHandler() {
        ServerSidePacketRegistry.INSTANCE.register(ATTACK_REQUEST, (context, packet) -> {
            context.getTaskQueue().execute(() -> {
                ItemStack playerHeldStack = context.getPlayer().getMainHandStack();

                if(playerHeldStack.getItem() instanceof AttackingItem) {
                    ((AttackingItem) playerHeldStack.getItem()).attack(context.getPlayer(), context.getPlayer().getEntityWorld(), playerHeldStack);
                }
            });
        });
    }
}
