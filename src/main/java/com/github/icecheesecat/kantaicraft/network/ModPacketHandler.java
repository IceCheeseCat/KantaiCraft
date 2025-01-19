package com.github.icecheesecat.kantaicraft.network;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.network.packet.DoEquipmentLevelUpPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncShipC2SPacket;
import com.github.icecheesecat.kantaicraft.network.packet.SyncShipS2CPacket;
import com.github.icecheesecat.kantaicraft.network.packet.TrajectoryPacket;
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
                SyncShipC2SPacket.class,
                SyncShipC2SPacket::encode,
                SyncShipC2SPacket::decode,
                SyncShipC2SPacket::serverHandle

        );

        INSTANCE.registerMessage(
                id++,
                SyncShipS2CPacket.class,
                SyncShipS2CPacket::encode,
                SyncShipS2CPacket::decode,
                SyncShipS2CPacket::clientHandle
        );

        INSTANCE.registerMessage(
                id++,
                DoEquipmentLevelUpPacket.class,
                DoEquipmentLevelUpPacket::encode,
                DoEquipmentLevelUpPacket::decode,
                DoEquipmentLevelUpPacket::handle
        );
    }
}
