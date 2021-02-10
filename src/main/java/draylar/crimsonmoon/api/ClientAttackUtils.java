package draylar.crimsonmoon.api;

import draylar.crimsonmoon.network.ServerNetworking;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class ClientAttackUtils {

    public static void requestAttack(ClientPlayerEntity player) {
        // tell server to call attack method on held item
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        ClientSidePacketRegistry.INSTANCE.sendToServer(ServerNetworking.ATTACK_REQUEST, packet);

        // call on client
        ItemStack playerHeldStack = player.getMainHandStack();
        if(playerHeldStack.getItem() instanceof AttackingItem) {
            ((AttackingItem) playerHeldStack.getItem()).attack(player, player.world, playerHeldStack);
        }
    }
}
