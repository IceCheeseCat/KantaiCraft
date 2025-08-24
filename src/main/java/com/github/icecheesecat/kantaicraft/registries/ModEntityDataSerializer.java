package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ShipLevel;
import com.github.icecheesecat.kantaicraft.entity.ship.ShipAnimationState;
import com.github.icecheesecat.kantaicraft.entity.ship.EmotionState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityDataSerializer {

    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, KantaiCraft.MODID);

    public static final RegistryObject<EntityDataSerializer<ShipAnimationState>> ANIMATION_STATE_SERIALIZER =
            ENTITY_DATA_SERIALIZERS.register("animation_state_serializer", () -> EntityDataSerializer.simpleEnum(ShipAnimationState.class));
    public static final RegistryObject<EntityDataSerializer<EmotionState>> EMOTION_STATE_SERIALIZER =
            ENTITY_DATA_SERIALIZERS.register("emotion_state_serializer", () -> EntityDataSerializer.simpleEnum(EmotionState.class));
    public static final RegistryObject<EntityDataSerializer<ShipLevel>> SHIP_LEVEL_SERIALIZER =
            ENTITY_DATA_SERIALIZERS.register("ship_level_serializer", () -> new EntityDataSerializer<ShipLevel>() {
                @Override
                public void write(FriendlyByteBuf pBuffer, ShipLevel pValue) {
                    pBuffer.writeInt(pValue.getLevel());
                    pBuffer.writeInt(pValue.getExp());
                }

                @Override
                public ShipLevel read(FriendlyByteBuf pBuffer) {
                    ShipLevel shipLevel = ShipLevel.levelZero();
                    shipLevel.setLevel(pBuffer.readInt());
                    shipLevel.setExp(pBuffer.readInt());
                    return shipLevel;
                }

                @Override
                public ShipLevel copy(ShipLevel pValue) {
                    ShipLevel shipLevel = ShipLevel.levelZero();
                    shipLevel.setLevel(pValue.getLevel());
                    shipLevel.setExp(pValue.getExp());
                    return shipLevel;
                }
            });
}
