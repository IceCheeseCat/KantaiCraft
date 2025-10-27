package com.github.icecheesecat.kantaicraft.equipment.entity;

import com.github.icecheesecat.kantaicraft.equipment.EquipmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EquipmentEntity extends Entity {
    public Entity relativeEntity;
    private Vec3 offsetFromRelativeEntity;
    private EquipmentType equipmentType;

    public EquipmentEntity(EntityType<? extends Entity> pEntityType, Level pLevel, EquipmentType equipmentType) {
        super(pEntityType, pLevel);
        this.equipmentType = equipmentType;
    }
    public void setRelativeToEntity(@NotNull Entity relativeEntity, Vec3 offsetFromRelativeEntity) {
        this.relativeEntity = relativeEntity;
        this.offsetFromRelativeEntity = offsetFromRelativeEntity;
    }

    @Override
    public Vec3 position() {
        if (this.relativeEntity == null || this.offsetFromRelativeEntity == null) {
            return super.position();
        }
        return this.relativeEntity.position().add(offsetFromRelativeEntity);
    }

    @Override
    public Vec2 getRotationVector() {
        if (this.relativeEntity == null)
            return super.getRotationVector();
        return this.relativeEntity.getRotationVector();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
//        if (pCompound.contains("relative_entity")) {
//            this.relativeEntity = ((ServerLevel) this.level()).getEntity(pCompound.getUUID("relative_entity"));
//        }
//        if (pCompound.contains("offset_from_relative")) {
//            this.offsetFromRelativeEntity = CompoundTagHelper.getVec3(pCompound.getCompound("offset_from_relative"));
//        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
//        if (this.relativeEntity != null) {
//            pCompound.putUUID("relative_entity", this.relativeEntity.getUUID());
//        }
//        if (this.offsetFromRelativeEntity != null) {
//            pCompound.put("offset_from_relative", CompoundTagHelper.putVec3(this.offsetFromRelativeEntity));
//        }
    }

    public static class __12cm_single_gun_mount__ extends EquipmentEntity implements CannonBarrelPosition {
        private static final Vec3 BARREL_POSITION = new Vec3(0.5f, 0.5f, 0.5f);
        public __12cm_single_gun_mount__(EntityType<? extends Entity> pEntityType, Level pLevel) {
            super(pEntityType, pLevel, EquipmentType.SMALL_CANNON);
        }

        @Override
        public Vec3 getPosition() {
            return new Vec3(0.5f, 0.5f, 0.5f);
        }
    }

    public static class __12cm_twin_gun_mount__ extends EquipmentEntity implements CannonBarrelPosition {
        public __12cm_twin_gun_mount__(EntityType<? extends Entity> pEntityType, Level pLevel) {
            super(pEntityType, pLevel, EquipmentType.SMALL_CANNON);
        }

        @Override
        public Vec3 getPosition() {
            return new Vec3(0.5f, 0.5f, 0.5f);
        }
    }


}
