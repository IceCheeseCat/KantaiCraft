package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlock;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockEntity;
import com.github.icecheesecat.kantaicraft.block.commandcenter.CommandCenterBlockItem;
import com.github.icecheesecat.kantaicraft.block.facilities.FacilityBlock;
import com.github.icecheesecat.kantaicraft.block.facilities.FacilityBlockEntity;
import com.github.icecheesecat.kantaicraft.block.facilities.fuelstation.FuelStationBlock;
import com.github.icecheesecat.kantaicraft.block.facilities.fuelstation.FuelStationBlockEntity;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlock;
import com.github.icecheesecat.kantaicraft.block.shipyard.ShipyardBlockEntity;
import com.github.icecheesecat.kantaicraft.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
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
    public static final RegistryObject<Block> COMMAND_CENTER = registerGeoBlock("command_center_block", () -> new CommandCenterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().strength(5.0f)), CommandCenterBlockItem::new);
    public static final RegistryObject<Block>  FACILITY = registerBlock("facility_block", FacilityBlock::new);
    public static final RegistryObject<Block> FUEL_STATION = registerBlock("fuel_station_block", FuelStationBlock::new);
    public static final RegistryObject<Block> SHIPYARD = registerBlock("shipyard_block", ShipyardBlock::new);

    public static final RegistryObject<BlockEntityType<CommandCenterBlockEntity>> COMMAND_CENTER_BETYPE =registerBlockEntity("command_center_block_entity", CommandCenterBlockEntity::new, COMMAND_CENTER);
    public static final RegistryObject<BlockEntityType<ShipyardBlockEntity>> SHIPYARD_BETYPE = registerBlockEntity("shipyard_block_entity", ShipyardBlockEntity::new, SHIPYARD);
    public static final RegistryObject<BlockEntityType<FuelStationBlockEntity>> FUEL_STATION_BETYPE = registerBlockEntity("fuel_station_block_entity", FuelStationBlockEntity::new, FUEL_STATION);
    public static final RegistryObject<BlockEntityType<FacilityBlockEntity>> FACILITY_BETYPE = registerBlockEntity("facility_block_entity", FacilityBlockEntity::new, FACILITY);


    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier) {
        return registerBlock(name, supplier, BlockItem::new);
    }

    private static <T extends BlockItem> RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier, BlockItemCreator<T> blockItemCreator) {
        RegistryObject<Block> ret = BLOCKS.register(name, supplier);
        ModItem.ITEMS.register(name, () -> blockItemCreator.create(ret.get(), new Item.Properties()));
        return ret;
    }

    private static <T extends BlockItem & GeoItem> RegistryObject<Block> registerGeoBlock(String name, Supplier<Block> supplier, GeoBlockItemCreator<T> creator) {
        RegistryObject<Block> ret = BLOCKS.register(name, supplier);
        ModItem.ITEMS.register(name, () -> creator.create(ret.get(), new Item.Properties()));
        return ret;
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier, RegistryObject<Block> blockRegistryObject) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(supplier, blockRegistryObject.get()).build(null));
    }

    @FunctionalInterface
    private interface BlockItemCreator<T extends BlockItem> {
        T create(Block block, Item.Properties properties);
    }

}
