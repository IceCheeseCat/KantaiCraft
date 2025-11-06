package com.github.icecheesecat.kantaicraft.capability;


import com.github.icecheesecat.kantaicraft.client.ClientPlayerKantaiDataCache;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegisterCapability
public class ClientPlayerKantaiDataCacheCapability implements ICapabilityProvider {

    public static final Capability<ClientPlayerKantaiDataCache> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    private ClientPlayerKantaiDataCache clientPlayerKantaiDataCache;
    LazyOptional<ClientPlayerKantaiDataCache> lazyClientPlayerKantaiDataCache = LazyOptional.of(this::getClientPlayerKantaiDataCache);

    public ClientPlayerKantaiDataCacheCapability() {
    }

    private ClientPlayerKantaiDataCache getClientPlayerKantaiDataCache() {
        if (this.clientPlayerKantaiDataCache == null) {
            this.clientPlayerKantaiDataCache = new ClientPlayerKantaiDataCache();
        }

        return this.clientPlayerKantaiDataCache;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ClientPlayerKantaiDataCacheCapability.TOKEN) {
            return lazyClientPlayerKantaiDataCache.cast();
        }

        return LazyOptional.empty();
    }

}
