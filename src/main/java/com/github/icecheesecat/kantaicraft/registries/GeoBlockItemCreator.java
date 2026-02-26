package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animatable.GeoItem;
@FunctionalInterface
public interface GeoBlockItemCreator<T extends BlockItem & GeoItem> {
     T create(Block block, Item.Properties properties);
}
