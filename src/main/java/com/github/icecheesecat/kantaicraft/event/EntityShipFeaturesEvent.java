package com.github.icecheesecat.kantaicraft.event;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import com.github.icecheesecat.kantaicraft.registries.ModMemoryModuleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = KantaiCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityShipFeaturesEvent {

    /**
     * 1. If entity used to target {@link  EntityShip}
     * 2. Else if entity was killed by {@link EntityShip}
     */
    @SubscribeEvent
    public static void onLivingDeathDrops(LivingDropsEvent event) {

        // drop mob is mad at entityShip
        if (event.getEntity() instanceof Mob mob && mob.getTarget() instanceof EntityShip entityShip) {
            addItemDropsToEntityShipPickUp(entityShip, new ArrayList<>(event.getDrops()));
        }
        // killed by entityShip
        else if (event.getSource().getEntity() instanceof EntityShip entityShip) {
            addItemDropsToEntityShipPickUp(entityShip, new ArrayList<>(event.getDrops()));
        }

    }

    private static void addItemDropsToEntityShipPickUp(EntityShip entityShip, List<ItemEntity> drops) {
        if (!entityShip.hasInventory()) return;

        if (!entityShip.getBrain().hasMemoryValue(ModMemoryModuleType.ITEMS_TO_PICK_UP.get())) {
            entityShip.getBrain().setMemory(ModMemoryModuleType.ITEMS_TO_PICK_UP.get(), drops);
        }
        else {
            entityShip.getBrain().getMemory(ModMemoryModuleType.ITEMS_TO_PICK_UP.get()).ifPresent(
                    itemEntities -> itemEntities.addAll(drops)
            );
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
            if (damageEntity instanceof EntityShip damageFromEntityShip && damageFromEntityShip.isShipOwner(player)) {
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

    @SubscribeEvent
    public static void onEntityShipGetsKill(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getSource().getEntity() instanceof EntityShip entityShip) {
            entityShip.addExp(event.getEntity().getExperienceReward());
        }
    }

}
