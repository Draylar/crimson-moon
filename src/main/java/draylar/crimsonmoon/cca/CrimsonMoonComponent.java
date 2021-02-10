package draylar.crimsonmoon.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import draylar.crimsonmoon.CrimsonMoon;
import draylar.crimsonmoon.CrimsonMoonClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class CrimsonMoonComponent implements ComponentV3, AutoSyncedComponent {

    private static final String CRIMSON_MOON_KEY = "CrimsonMoon";
    private final World world;
    private boolean isCrimsonMoon = false;

    public CrimsonMoonComponent(World world) {
        this.world = world;
    }

    public boolean isCrimsonMoon() {
        return isCrimsonMoon;
    }

    public void setCrimsonMoon(boolean toggle) {
        this.isCrimsonMoon = toggle;
        CrimsonMoon.CRIMSON_MOON_COMPONENT.sync(world);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.isCrimsonMoon = tag.getBoolean(CRIMSON_MOON_KEY);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean(CRIMSON_MOON_KEY, isCrimsonMoon);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        CompoundTag tag = buf.readCompoundTag();

        if (tag != null) {
            readFromNbt(tag);

            if(isCrimsonMoon) {
                CrimsonMoonClient.triggerBanner();
            }
        }
    }
}
