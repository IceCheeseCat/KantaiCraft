package com.github.icecheesecat.kantaicraft.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public class SerializedLivingEntity implements INBTSerializable<CompoundTag> {

    private EntityType<?> entityType;
    private UUID uuid;
    private CompoundTag entityTag;

    private SerializedLivingEntity() {}

    public SerializedLivingEntity(LivingEntity livingEntity) {
        this.entityType = livingEntity.getType();
        this.uuid = livingEntity.getUUID();
        CompoundTag nbt = new CompoundTag();
        livingEntity.addAdditionalSaveData(nbt);
        this.entityTag = nbt;
    }

    public static SerializedLivingEntity createFromNbt(CompoundTag nbt) {

        var ses = new SerializedLivingEntity();
        ses.deserializeNBT(nbt);

        return ses;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("entityType", this.entityType.toString());
        nbt.putUUID("uuid", this.uuid);
        nbt.put("entityTag", this.entityTag);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        EntityType.byString(nbt.getString("entityType")).ifPresent(entityType1 -> {
            this.entityType = entityType1;
        });
        this.uuid = nbt.getUUID("uuid");
        this.entityTag = nbt.getCompound("entityTag");
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CompoundTag getEntityTag() {
        return entityTag;
    }
}
