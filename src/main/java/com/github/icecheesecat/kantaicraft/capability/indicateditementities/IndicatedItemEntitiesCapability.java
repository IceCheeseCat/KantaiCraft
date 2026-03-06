package com.github.icecheesecat.kantaicraft.capability.indicateditementities;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class IndicatedItemEntitiesCapability implements ICapabilityProvider {

    public static final Capability<IndicatedItemEntities> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    private IndicatedItemEntities IndicatedItemEntities = null;
    private final LazyOptional<IndicatedItemEntities> lazyIndicatedItemEntities = LazyOptional.of(this::getIndicatedItemEntities);

    public IndicatedItemEntitiesCapability() {
    }

    private IndicatedItemEntities getIndicatedItemEntities() {
        if (this.IndicatedItemEntities == null) {
            this.IndicatedItemEntities = new IndicatedItemEntities();
        }

        return this.IndicatedItemEntities;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == IndicatedItemEntitiesCapability.TOKEN) {
            return lazyIndicatedItemEntities.cast();
        }

        return LazyOptional.empty();
    }
}
