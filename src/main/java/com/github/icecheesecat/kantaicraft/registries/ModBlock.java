package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.PortBlock;
import com.github.icecheesecat.kantaicraft.block.PortBlockEntity;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlock;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockEntity;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockItem;
import com.github.icecheesecat.kantaicraft.block.patternblock.ComponentBlock;
import com.github.icecheesecat.kantaicraft.block.patternblock.ComponentBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardCoreBlock;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.function.Supplier;

public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KantaiCraft.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<Block> PORT = registerBlock("port", () -> new PortBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> SHIPYARD_CORE = registerBlock("shipyard_block", () -> new ShipyardCoreBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> FLOOR = registerBlock("floor_block", () -> new ComponentBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> CRANE = registerBlock("crane_block", () -> new ComponentBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> COMMAND_CENTER = registerGeoBlock("command_center_block", () -> new CommandCenterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()), CommandCenterBlockItem::new);

    public static final RegistryObject<BlockEntityType<PortBlockEntity>> PORT_BETYPE = BLOCK_ENTITIES.register("port_block_entity_type", () -> BlockEntityType.Builder.of(PortBlockEntity::new, PORT.get()).build(null));
    public static final RegistryObject<BlockEntityType<ComponentBlockEntity>> COMPONENT_BETYPE = BLOCK_ENTITIES.register("component_block_entity_type", () -> BlockEntityType.Builder.of(ComponentBlockEntity::new, FLOOR.get(), CRANE.get()).build(null));
//    public static final RegistryObject<BlockEntityType<CoreBlockEntity>> CORE_BETYPE = BLOCK_ENTITIES.register("core_block_entity_type", () -> BlockEntityType.Builder.of(CoreBlockEntity::new, SHIPYARD_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShipyardBlockEntity>> SHIPYARD_BETPYE = BLOCK_ENTITIES.register("shipyard_block_entity", () -> BlockEntityType.Builder.of(ShipyardBlockEntity::new, SHIPYARD_CORE.get()).build(null));
    public static final RegistryObject<BlockEntityType<CommandCenterBlockEntity>> COMMAND_CENTER_BETYPE = BLOCK_ENTITIES.register("command_center_block_entity", () -> BlockEntityType.Builder.of(CommandCenterBlockEntity::new, COMMAND_CENTER.get()).build(null));

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier) {
        RegistryObject<Block> ret = BLOCKS.register(name, supplier);
        ModItem.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties()));
        return ret;
    }

    private static <T extends BlockItem & GeoItem> RegistryObject<Block> registerGeoBlock(String name, Supplier<Block> supplier, GeoBlockItemCreator<T> creator) {
        RegistryObject<Block> ret = BLOCKS.register(name, supplier);
        ModItem.ITEMS.register(name, () -> creator.create(ret.get(), new Item.Properties()));
        return ret;
    }



}
