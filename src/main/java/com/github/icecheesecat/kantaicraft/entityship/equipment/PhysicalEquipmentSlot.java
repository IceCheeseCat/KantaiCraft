package com.github.icecheesecat.kantaicraft.entityship.equipment;

import com.github.icecheesecat.kantaicraft.capability.EquipmentHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PhysicalEquipmentSlot {

    final List<Vec3> positions;

    public PhysicalEquipmentSlot(int size) {
        this.positions = Collections.nCopies(size, null);
    }

    public Vec3 getSlotPosition(int i) {
        assert i < this.positions.size();
        return this.positions.get(i);
    }

}
