package com.github.icecheesecat.kantaicraft.block.commandcenter;

import com.github.icecheesecat.kantaicraft.block.TwoPart;
import com.github.icecheesecat.kantaicraft.block.TwoPartBlock;
import com.github.icecheesecat.kantaicraft.menu.commandcenter.CommandCenterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import org.jetbrains.annotations.Nullable;

public class CommandCenterBlock extends TwoPartBlock implements EntityBlock {

    private static final Component MENU_TITLE = Component.translatable("command_center_menu");
    private static final VoxelShape TABLE = Block.box(0.0d, 11.0d, 0.0d, 16.0f, 16.0d, 16.0d);
    private static final VoxelShape EAST_SHAPE_FRONT;
    private static final VoxelShape EAST_SHAPE_BACK;
    private static final VoxelShape WEST_SHAPE_FRONT;
    private static final VoxelShape WEST_SHAPE_BACK;
    private static final VoxelShape NORTH_SHAPE_FRONT;
    private static final VoxelShape NORTH_SHAPE_BACK;
    private static final VoxelShape SOUTH_SHAPE_FRONT;
    private static final VoxelShape SOUTH_SHAPE_BACK;

    static {
        VoxelShape leg = Block.box(14, 0, 0, 16, 11, 16);
        VoxelShape back_cover = Block.box(0, 0, 14, 14, 11, 16);
        EAST_SHAPE_FRONT = Shapes.or(TABLE, leg, back_cover);

        VoxelShape leg1 = Block.box(0, 0, 0, 2, 11, 16);
        VoxelShape back_cover1 = Block.box(2, 0, 14, 16, 11, 16);
        EAST_SHAPE_BACK = Shapes.or(TABLE, leg1, back_cover1);

        VoxelShape leg2 = Block.box(0, 0, 0, 2, 11, 16);
        VoxelShape back_cover2 = Block.box(2, 0, 0, 16, 11, 2);
        WEST_SHAPE_FRONT = Shapes.or(TABLE, leg2, back_cover2);

        VoxelShape leg3 = Block.box(14, 0, 0, 16, 11, 16);
        VoxelShape back_cover3 = Block.box(0, 0, 0, 14, 11, 2);
        WEST_SHAPE_BACK = Shapes.or(TABLE, leg3, back_cover3);

        VoxelShape leg4 = Block.box(0, 0, 0, 16, 11, 2);
        VoxelShape back_cover4 = Block.box(14, 0, 0, 16, 11, 16);
        NORTH_SHAPE_FRONT = Shapes.or(TABLE, leg4, back_cover4);

        VoxelShape leg5 = Block.box(0, 0, 14, 16, 11, 16);
        VoxelShape back_cover5 = Block.box(14, 0, 0, 16, 11, 16);
        NORTH_SHAPE_BACK = Shapes.or(TABLE, leg5, back_cover5);

        VoxelShape leg6 = Block.box(0, 0, 14, 16, 11, 16);
        VoxelShape back_cover6 = Block.box(0, 0, 0, 2, 11, 16);
        SOUTH_SHAPE_FRONT = Shapes.or(TABLE, leg6, back_cover6);

        VoxelShape leg7 = Block.box(0, 0, 0, 16, 11, 2);
        VoxelShape back_cover7 = Block.box(0, 0, 0, 2, 11, 16);
        SOUTH_SHAPE_BACK = Shapes.or(TABLE, leg7, back_cover7);
    }

    public CommandCenterBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        pPlayer.openMenu(this.getMenuProvider(pState, pLevel, pPos));
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((id, inventory, player) ->
            new CommandCenterMenu(id, inventory, ContainerLevelAccess.create(player.level(), pPos))
        , MENU_TITLE);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        TwoPart part = pState.getValue(TWO_PART);
        if (direction == Direction.EAST) {
            if (part == TwoPart.Front) {
                return EAST_SHAPE_FRONT;
            }
            else {
                return EAST_SHAPE_BACK;
            }
        } else if (direction == Direction.WEST) {
            if (part == TwoPart.Front) {
                return WEST_SHAPE_FRONT;
            }
            else {
                return WEST_SHAPE_BACK;
            }
        }
        else if (direction == Direction.NORTH) {
            if (part == TwoPart.Front) {
                return NORTH_SHAPE_FRONT;
            }
            else {
                return NORTH_SHAPE_BACK;
            }
        } else if (direction == Direction.SOUTH) {
            if (part == TwoPart.Front) {
                return SOUTH_SHAPE_FRONT;
            }
            else {
                return SOUTH_SHAPE_BACK;
            }
        }

        return Shapes.empty();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CommandCenterBlockEntity(pPos, pState);
    }

}
