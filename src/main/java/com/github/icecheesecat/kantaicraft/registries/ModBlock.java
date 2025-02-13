package com.github.icecheesecat.kantaicraft.registries;

import com.github.icecheesecat.kantaicraft.KantaiCraft;
import com.github.icecheesecat.kantaicraft.block.PortBlock;
import com.github.icecheesecat.kantaicraft.block.PortBlockEntity;
import com.github.icecheesecat.kantaicraft.block.ShipyardBlock;
import com.github.icecheesecat.kantaicraft.block.ShipyardBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlock {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KantaiCraft.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KantaiCraft.MODID);

    public static final RegistryObject<Block> PORT = BLOCKS.register("port", () -> new PortBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> SHIPYARD = BLOCKS.register("shipyard", () -> new ShipyardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<BlockEntityType<PortBlockEntity>> PORT_BETYPE = BLOCK_ENTITIES.register("port_block_entity_type", () -> BlockEntityType.Builder.of(PortBlockEntity::new, PORT.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShipyardBlockEntity>> SHIPYARD_BETYPE = BLOCK_ENTITIES.register("shipyard_block_entity_type", () -> BlockEntityType.Builder.of(ShipyardBlockEntity::new, SHIPYARD.get()).build(null));

}
