package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.*;
import com.github.icecheesecat.kantaicraft.block.basic.ComponentBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.C;

import java.util.Set;
import java.util.function.Supplier;

public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KantaiCraft.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<Block> PORT = registerBlock("port", () -> new PortBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> SHIPYARD = registerBlock("shipyard", () -> new ShipyardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> FLOOR = registerBlock("floor_block", () -> new FloorBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> CRANE = registerBlock("crane_block", () -> new FloorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<BlockEntityType<PortBlockEntity>> PORT_BETYPE = BLOCK_ENTITIES.register("port_block_entity_type", () -> BlockEntityType.Builder.of(PortBlockEntity::new, PORT.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShipyardBlockEntity>> SHIPYARD_BETYPE = BLOCK_ENTITIES.register("shipyard_block_entity_type", () -> BlockEntityType.Builder.of(ShipyardBlockEntity::new, SHIPYARD.get()).build(null));
    public static final RegistryObject<BlockEntityType<ComponentBlockEntity>> COMPONENT_BETYPE = BLOCK_ENTITIES.register("component_block_entity_type", () -> BlockEntityType.Builder.of(ComponentBlockEntity::new, FLOOR.get()).build(null));

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier) {
        RegistryObject<Block> ret = BLOCKS.register(name, supplier);
        ModItem.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties()));
        return ret;
    }

}
