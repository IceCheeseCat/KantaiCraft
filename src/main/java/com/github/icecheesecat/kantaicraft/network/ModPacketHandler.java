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
                SyncShipPacket.class,
                SyncShipPacket::encode,
                SyncShipPacket::decode,
                SyncShipPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                ClientRemoveTrajectoryPacket.class,
                ClientRemoveTrajectoryPacket::encode,
                ClientRemoveTrajectoryPacket::decode,
                ClientRemoveTrajectoryPacket::handle
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
                ShipyardBuiltDataPacket.class,
                ShipyardBuiltDataPacket::encode,
                ShipyardBuiltDataPacket::decode,
                ShipyardBuiltDataPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                ShipyardSpawnBuiltDataPacket.class,
                ShipyardSpawnBuiltDataPacket::encode,
                ShipyardSpawnBuiltDataPacket::decode,
                ShipyardSpawnBuiltDataPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                PlayerKantaiDataPacket.class,
                PlayerKantaiDataPacket::encode,
                PlayerKantaiDataPacket::decode,
                PlayerKantaiDataPacket::handle
        );
    }

}
