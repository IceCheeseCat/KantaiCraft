package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class ShipSpawnEgg extends Item {

    final EntityType<? extends BasicEntityShip> entityType;
    final boolean ownedByUser;

    public ShipSpawnEgg(Properties pProperties, Supplier<EntityType<? extends BasicEntityShip>> entityType, boolean ownedByUser) {
        super(pProperties);
        this.entityType = entityType.get();
        this.ownedByUser = ownedByUser;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (level.isClientSide) return InteractionResult.PASS;

        BlockPos clickedPos = pContext.getClickedPos();
        BasicEntityShip entityShip = entityType.create(level);
        if (ownedByUser) {
            entityShip.setOwner(pContext.getPlayer().getUUID());
            entityShip.setPos(clickedPos.getCenter());
        }
        level.addFreshEntity(entityShip);

        return InteractionResult.SUCCESS;
    }
}
