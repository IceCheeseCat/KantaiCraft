package com.github.icecheesecat.kantaicraft.block.expedition;

import com.github.icecheesecat.kantaicraft.block.TwoPart;
import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.menu.expedition.ExpeditionMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class ExpeditionBlock extends TwoPartBlock implements EntityBlock {

    private static final Component MENU_TITLE = Component.translatable("menu.kantaicraft.expedition");

    private static final VoxelShape TABLE = Block.box(0, 11, 0, 16, 16, 16);
    private static final VoxelShape EAST_SHAPE_FRONT = Shapes.or(
            TABLE, Block.box(14, 0, 0, 16, 11, 16), Block.box(0, 0, 14, 14, 11, 16));
    private static final VoxelShape EAST_SHAPE_BACK = Shapes.or(
            TABLE, Block.box(0, 0, 0, 2, 11, 16), Block.box(2, 0, 14, 16, 11, 16));
    private static final VoxelShape WEST_SHAPE_FRONT = Shapes.or(
            TABLE, Block.box(0, 0, 0, 2, 11, 16), Block.box(2, 0, 0, 16, 11, 2));
    private static final VoxelShape WEST_SHAPE_BACK = Shapes.or(
            TABLE, Block.box(14, 0, 0, 16, 11, 16), Block.box(0, 0, 0, 14, 11, 2));
    private static final VoxelShape NORTH_SHAPE_FRONT = Shapes.or(
            TABLE, Block.box(0, 0, 0, 16, 11, 2), Block.box(14, 0, 0, 16, 11, 16));
    private static final VoxelShape NORTH_SHAPE_BACK = Shapes.or(
            TABLE, Block.box(0, 0, 14, 16, 11, 16), Block.box(14, 0, 0, 16, 11, 16));
    private static final VoxelShape SOUTH_SHAPE_FRONT = Shapes.or(
            TABLE, Block.box(0, 0, 14, 16, 11, 16), Block.box(0, 0, 0, 2, 11, 16));
    private static final VoxelShape SOUTH_SHAPE_BACK = Shapes.or(
            TABLE, Block.box(0, 0, 0, 16, 11, 2), Block.box(0, 0, 0, 2, 11, 16));

    public ExpeditionBlock(Properties p_properties) {
        super(p_properties);
    }

    @Override
    public InteractionResult use(BlockState p_state, Level p_level, BlockPos p_pos, Player p_player,
                                 InteractionHand p_hand, BlockHitResult p_hit) {
        if (p_level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        NetworkHooks.openScreen((ServerPlayer) p_player, getMenuProvider(p_state, p_level, p_pos), p_pos);
        return InteractionResult.CONSUME;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState p_state, Level p_level, BlockPos p_pos) {
        return new SimpleMenuProvider((p_containerId, p_inventory, p_player) ->
                new ExpeditionMenu(p_containerId, p_inventory, ContainerLevelAccess.create(p_level, p_pos)),
                MENU_TITLE);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState p_state, BlockGetter p_level, BlockPos p_pos,
                              CollisionContext p_context) {
        boolean isFront = p_state.getValue(TWO_PART) == TwoPart.Front;

        return switch (p_state.getValue(FACING)) {
            case EAST -> isFront ? EAST_SHAPE_FRONT : EAST_SHAPE_BACK;
            case WEST -> isFront ? WEST_SHAPE_FRONT : WEST_SHAPE_BACK;
            case NORTH -> isFront ? NORTH_SHAPE_FRONT : NORTH_SHAPE_BACK;
            case SOUTH -> isFront ? SOUTH_SHAPE_FRONT : SOUTH_SHAPE_BACK;
            default -> Shapes.empty();
        };
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_pos, BlockState p_state) {
        return new ExpeditionBlockEntity(p_pos, p_state);
    }
}
