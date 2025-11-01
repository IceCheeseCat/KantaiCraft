package com.github.icecheesecat.kantaicraft.model.equipment;

import com.github.icecheesecat.kantaicraft.entityship.entity.EquippableSlots;
import com.github.icecheesecat.kantaicraft.util.CompoundTagHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquippableDetailSlots extends EquippableSlots {

    Map<String, Vector3d> offsetFromModel = new HashMap<>();

    public EquippableDetailSlots() {
    }

    public EquippableDetailSlots(Map<String, Vector3d> offsetFromModel) {
        this.offsetFromModel = offsetFromModel;
    }

    public Vector3d getOffsetFromModel(String boneName) {
        return this.offsetFromModel.getOrDefault(boneName, new Vector3d());
    }

    public static EquippableDetailSlots createFourEquipments(List<Vector3d> offsetFromModels) {
        if (offsetFromModels.size() != 4) {
            throw new IllegalStateException("Creating 4 equipments " + EquippableSlots.class.getName() + " parameter {offsetFromModels} must be length of 4");
        }

        var eds = new EquippableDetailSlots();
        for (int i = 0; i < offsetFromModels.size(); i++) {
            eds.offsetFromModel.put("equipment_"+i, offsetFromModels.get(i));
        }

        return eds;
    }
}
