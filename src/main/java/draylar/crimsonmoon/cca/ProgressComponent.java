package draylar.crimsonmoon.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.CompoundTag;

public class ProgressComponent implements ComponentV3 {

    private static final String VISITED_NETHER_KEY = "VisitedNether";
    private boolean hasVisitedNether = false;

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.hasVisitedNether = tag.getBoolean(VISITED_NETHER_KEY);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean(VISITED_NETHER_KEY, hasVisitedNether);
    }

    public boolean hasVisitedNether() {
        return hasVisitedNether;
    }

    public void setHasVisitedNether(boolean hasVisitedNether) {
        this.hasVisitedNether = hasVisitedNether;
    }
}
