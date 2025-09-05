package com.github.icecheesecat.kantaicraft.network;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.network.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(KantaiCraft.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int id = 0;

    public static void registerMessages() {
        INSTANCE.registerMessage(
                id++,
                TrajectoryPacket.class,
                TrajectoryPacket::encode,
                TrajectoryPacket::decode,
                TrajectoryPacket::clientHandle
        );

        INSTANCE.registerMessage(
                id++,
                TogglePlayerShipPacket.class,
                TogglePlayerShipPacket::encode,
                TogglePlayerShipPacket::decode,
                TogglePlayerShipPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                ClientSetDestroyTrajectoryPacket.class,
                ClientSetDestroyTrajectoryPacket::encode,
                ClientSetDestroyTrajectoryPacket::decode,
                ClientSetDestroyTrajectoryPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                S2CEquipmentOptionsPacket.class,
                S2CEquipmentOptionsPacket::encode,
                S2CEquipmentOptionsPacket::decode,
                S2CEquipmentOptionsPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                C2SEquipmentOptionsPacket.class,
                C2SEquipmentOptionsPacket::encode,
                C2SEquipmentOptionsPacket::decode,
                C2SEquipmentOptionsPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                ShipyardPacket.class,
                ShipyardPacket::encode,
                ShipyardPacket::decode,
                ShipyardPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                ShipyardSpawnEntityPacket.class,
                ShipyardSpawnEntityPacket::encode,
                ShipyardSpawnEntityPacket::decode,
                ShipyardSpawnEntityPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                PlayerKantaiDataPacket.class,
                PlayerKantaiDataPacket::encode,
                PlayerKantaiDataPacket::decode,
                PlayerKantaiDataPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                SetShipyardSlotOwnerPacket.class,
                SetShipyardSlotOwnerPacket::encode,
                SetShipyardSlotOwnerPacket::decode,
                SetShipyardSlotOwnerPacket::handle
        );
    }

}
