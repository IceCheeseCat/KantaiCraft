package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entity.ship.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvent {

    @SubscribeEvent
    public static void livingEntityTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getEntity().level().getGameTime() % 100 != 0) return;
    }

    @SubscribeEvent
    public static void onLivingDeathDrops(LivingDropsEvent event) {

        if (event.getSource().getEntity() instanceof EntityShip ship) {
            if (!ship.hasInventory()) return;

            List<ItemEntity> drops = new ArrayList<>(event.getDrops());

            if (!ship.getBrain().hasMemoryValue(ModMemoryModuleType.KILLED_ENTITY_DROPS.get())) {
                 ship.getBrain().setMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get(), drops);
            }
            else {
                ship.getBrain().getMemory(ModMemoryModuleType.KILLED_ENTITY_DROPS.get()).ifPresent(
                    itemEntities -> itemEntities.addAll(drops)
                );
            }


        }

    }

    /**
     * When player gets hurt, announce {@link EntityShip} helps admiral fight entity damages him
     */
    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent event) {
        Entity damageEntity = event.getSource().getEntity();
        if (event.getEntity() instanceof Player player && damageEntity != null) {
            if (!(damageEntity instanceof LivingEntity)) {
                return;
            }
            if (damageEntity instanceof EntityShip damageFromShip && damageFromShip.isShipOwner(player)) {
                return;
            }
            Level level = event.getEntity().level();
            List<Entity> entityList = level.getEntities(player, AABB.ofSize(player.getEyePosition(), 40, 40, 40), entity -> entity instanceof EntityShip entityShip && entityShip.isShipOwner(player));
            entityList.forEach(entity -> {
                if (entity instanceof EntityShip entityShip) {
                    entityShip.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, (LivingEntity) damageEntity);
                }
            });
        }
    }


}
