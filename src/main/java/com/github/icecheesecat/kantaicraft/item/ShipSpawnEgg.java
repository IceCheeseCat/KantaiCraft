package com.github.icecheesecat.kantaicraft.item;

import com.github.icecheesecat.kantaicraft.entityship.entity.EntityShip;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ShipSpawnEgg extends ForgeSpawnEggItem {
    final BiConsumer<EntityShip, Player> consumer;

    public ShipSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, BiConsumer<EntityShip, Player> consumer, Properties props) {
        super(type, backgroundColor, highlightColor, props);
        this.consumer = consumer;
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (level.isClientSide) return InteractionResult.PASS;

        BlockPos clickedPos = pContext.getClickedPos();
        ItemStack itemStack = pContext.getItemInHand();

        var entityShip = this.getType(itemStack.getTag());
        EntityShip ship = (EntityShip) entityShip.spawn((ServerLevel) level, pContext.getItemInHand(), pContext.getPlayer(), clickedPos, MobSpawnType.SPAWN_EGG, true, false);
        consumer.accept(ship, pContext.getPlayer());
        itemStack.shrink(1);

        return InteractionResult.CONSUME;
    }
}
