package net.mehvahdjukaar.tron_digitized.common.entity;

public interface IHealableEntity {
    void setInHealingChamber(boolean state);

    boolean isInHealingChamber();

    float getHealingFade(float partialTicks);
}
