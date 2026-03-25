package com.github.icecheesecat.kantaicraft.network;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.network.packet.commandcenter.DispatchEntityShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.commandcenter.RetrieveEntityShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.entityship.DropIndicationPacket;
import com.github.icecheesecat.kantaicraft.network.packet.entityship.LavaFuelPacket;
import com.github.icecheesecat.kantaicraft.network.packet.entityship.TogglePlayerShipPacket;
import com.github.icecheesecat.kantaicraft.network.packet.equipment.EquipmentHandlerPacket;
import com.github.icecheesecat.kantaicraft.network.packet.equipment.EquipmentOptionsPacket;
import com.github.icecheesecat.kantaicraft.network.packet.equipment.RequestEquipmentOptionsPacket;
import com.github.icecheesecat.kantaicraft.network.packet.fuelstation.FuelStationPacket;
import com.github.icecheesecat.kantaicraft.network.packet.playerkantaidata.PlayerKantaiDataPacket;
import com.github.icecheesecat.kantaicraft.network.packet.shipyard.SetShipyardSlotOwnerPacket;
import com.github.icecheesecat.kantaicraft.network.packet.shipyard.ShipyardPacket;
import com.github.icecheesecat.kantaicraft.network.packet.shipyard.ShipyardSpawnEntityPacket;
import com.github.icecheesecat.kantaicraft.network.packet.trajectory.ClientSetDestroyTrajectoryPacket;
import com.github.icecheesecat.kantaicraft.network.packet.trajectory.TrajectoryPacket;
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
                EquipmentOptionsPacket.class,
                EquipmentOptionsPacket::encode,
                EquipmentOptionsPacket::decode,
                EquipmentOptionsPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                RequestEquipmentOptionsPacket.class,
                RequestEquipmentOptionsPacket::encode,
                RequestEquipmentOptionsPacket::decode,
                RequestEquipmentOptionsPacket::handle
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
                PlayerKantaiDataPacket.Request.class,
                PlayerKantaiDataPacket.Request::encode,
                PlayerKantaiDataPacket.Request::decode,
                PlayerKantaiDataPacket.Request::handle
        );

        INSTANCE.registerMessage(
                id++,
                SetShipyardSlotOwnerPacket.class,
                SetShipyardSlotOwnerPacket::encode,
                SetShipyardSlotOwnerPacket::decode,
                SetShipyardSlotOwnerPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                EquipmentHandlerPacket.class,
                EquipmentHandlerPacket::encode,
                EquipmentHandlerPacket::decode,
                EquipmentHandlerPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                DispatchEntityShipPacket.class,
                DispatchEntityShipPacket::encode,
                DispatchEntityShipPacket::decode,
                DispatchEntityShipPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                DropIndicationPacket.class,
                DropIndicationPacket::encode,
                DropIndicationPacket::decode,
                DropIndicationPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                LavaFuelPacket.class,
                LavaFuelPacket::encode,
                LavaFuelPacket::decode,
                LavaFuelPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                FuelStationPacket.class,
                FuelStationPacket::encode,
                FuelStationPacket::decode,
                FuelStationPacket::handle
        );

        INSTANCE.registerMessage(
                id++,
                RetrieveEntityShipPacket.class,
                RetrieveEntityShipPacket::encode,
                RetrieveEntityShipPacket::decode,
                RetrieveEntityShipPacket::handle
        );

    }

}
