package com.github.icecheesecat.kantaicraft.capability.kantaidata;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class SerializedEntityShip implements INBTSerializable<CompoundTag> {

    private EntityType<?> entityType;
    private UUID uuid;
    private CompoundTag playerKantaiDataTag;

    private SerializedEntityShip() {}

    public SerializedEntityShip(EntityShip entityShip) {
        this.entityType = entityShip.getType();
        this.uuid = entityShip.getUUID();
        this.playerKantaiDataTag = entityShip.saveAsPlayerKantaiDataTag();
    }

    public static SerializedEntityShip createFromNbt(CompoundTag nbt) {

        var ses = new SerializedEntityShip();
        ses.deserializeNBT(nbt);

        return ses;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("entityType", this.entityType.toString());
        nbt.putUUID("uuid", this.uuid);
        nbt.put("playerKantaiDataTag", this.playerKantaiDataTag);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.entityType = ModEntity.getByName(nbt.getString("entityType"));
        this.uuid = nbt.getUUID("uuid");
        this.playerKantaiDataTag = nbt.getCompound("playerKantaiData");
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CompoundTag getPlayerKantaiDataTag() {
        return playerKantaiDataTag;
    }
}
