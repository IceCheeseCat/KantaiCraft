package com.github.icecheesecat.kantaicraft.model.equipment;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import java.util.*;

public class PhysicalEquipmentPosition {
    Map<BodyPart, BodyPart.Position> bodyPartPositions;

    public PhysicalEquipmentPosition() {
        this.bodyPartPositions = new HashMap<>();
    }

    private void setupBakedPosition(BakedGeoModel bakedGeoModel) {
        Arrays.stream(BodyPart.values()).forEach(bodyPart -> {
            var boneOptional = bakedGeoModel.getBone(bodyPart.name());
            if (boneOptional.isEmpty())
                this.bodyPartPositions.put(bodyPart, new BodyPart.Position(new Vector3d(), new Vector3d()));
            else
                this.bodyPartPositions.put(bodyPart, new BodyPart.Position(boneOptional.get().getLocalPosition(), new Vector3d()));
        });
    }

    public void setupBodyPartPosition(BakedGeoModel bakedGeoModel, Map<BodyPart, Vector3d> mapOfOffsets) {
        this.setupBakedPosition(bakedGeoModel);
        mapOfOffsets.forEach(this::putOffsetToWeapon);
    }

    @Nullable
    public BodyPart.Position getBodyPartPosition(BodyPart bodyPart) {
        return this.bodyPartPositions.get(bodyPart);
    }

    private void putOffsetToWeapon(BodyPart bodyPart, Vector3d offsetToWeapon) {
        if (this.bodyPartPositions.containsKey(bodyPart)) {
            this.bodyPartPositions.put(bodyPart, new BodyPart.Position(this.bodyPartPositions.get(bodyPart).bodyPartPosition(), offsetToWeapon));
        }
        else {
            throw new RuntimeException(this.getClass().getName() + " map of <BodyPart, Position> must have position of baked position setup first.");
        }
    }

}
