package draylar.crimsonmoon.impl;

import draylar.crimsonmoon.api.ClientAttackUtils;
import draylar.crimsonmoon.registry.CrimsonItems;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

public class AttackKeyHandler implements ClientTickEvents.StartTick {

    @Override
    public void onStartTick(MinecraftClient client) {
        if (client.player == null) return;

        if (client.options.attackKey.isPressed()) {
            if (client.player.getMainHandStack().getItem().equals(CrimsonItems.CARNAGE)) {
                client.player.swingHand(Hand.MAIN_HAND);

                // this is a horrific idea
                // dear standard code readers: PRETEND YOU SAW NOTHING
                // dear people who just had an idea: STOP RIGHT THERE
                if (client.player.age % 5 == 0) {
                    ClientAttackUtils.requestAttack(client.player);
                }
            }
        }
    }
}
