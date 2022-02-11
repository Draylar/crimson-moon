package draylar.crimsonmoon.impl.server;

import draylar.crimsonmoon.api.AttackingItem;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class AttackPacketHandler implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            ItemStack playerHeldStack = player.getMainHandStack();
            if(playerHeldStack.getItem() instanceof AttackingItem) {
                ((AttackingItem) playerHeldStack.getItem()).attack(player, player.getEntityWorld(), playerHeldStack);
            }
        });
    }
}
