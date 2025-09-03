package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.ShipLeveling;
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
    public static final RegistryObject<EntityDataSerializer<ShipLeveling>> SHIP_LEVEL_SERIALIZER =
            ENTITY_DATA_SERIALIZERS.register("ship_level_serializer", () -> new EntityDataSerializer<ShipLeveling>() {
                @Override
                public void write(FriendlyByteBuf pBuffer, ShipLeveling pValue) {
                    pBuffer.writeInt(pValue.getLevel());
                    pBuffer.writeInt(pValue.getExp());
                }

                @Override
                public ShipLeveling read(FriendlyByteBuf pBuffer) {
                    ShipLeveling shipLeveling = ShipLeveling.levelZero();
                    shipLeveling.setLevel(pBuffer.readInt());
                    shipLeveling.setExp(pBuffer.readInt());
                    return shipLeveling;
                }

                @Override
                public ShipLeveling copy(ShipLeveling pValue) {
                    ShipLeveling shipLeveling = ShipLeveling.levelZero();
                    shipLeveling.setLevel(pValue.getLevel());
                    shipLeveling.setExp(pValue.getExp());
                    return shipLeveling;
                }
            });

}
