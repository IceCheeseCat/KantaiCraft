package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.entity.ship.BasicEntityShip;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ShipSpawnEgg extends ForgeSpawnEggItem {

    final boolean ownedByUser;
    final Consumer<BasicEntityShip> consumer;

    public ShipSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, boolean ownedByUser, Consumer<BasicEntityShip> consumer, Properties props) {
        super(type, backgroundColor, highlightColor, props);
        this.ownedByUser = ownedByUser;
        this.consumer = consumer;
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (level.isClientSide) return InteractionResult.PASS;

        BlockPos clickedPos = pContext.getClickedPos();
        ItemStack itemStack = pContext.getItemInHand();
        var entityShip = this.getType(itemStack.getTag());
        if (ownedByUser) {
            BasicEntityShip ship = (BasicEntityShip) entityShip.spawn((ServerLevel) level, pContext.getItemInHand(), pContext.getPlayer(), clickedPos, MobSpawnType.SPAWN_EGG, true, false);
//            ship.getEquipmentSlots().getEquipments();
            ship.setAmmo(100.0f);
            consumer.accept(ship);
            ship.setOwner(pContext.getPlayer().getUUID());
            itemStack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}
