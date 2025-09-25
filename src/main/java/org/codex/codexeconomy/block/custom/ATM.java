package org.codex.codexeconomy.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.codex.codexeconomy.rendering.screens.atm.MainMenu;

public class ATM extends HorizontalFacingBlock {
    public static final MapCodec<ATM> CODEC = Block.createCodec(ATM::new);

    public ATM(Settings settings){
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends ATM> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(FACING);
        return switch (dir) {
            case NORTH -> VoxelShapes.cuboid(0.3f, 0.1f, 0.1f, 0.9f, 1.85f, 0.9f);
            case SOUTH -> VoxelShapes.cuboid(0.3f, 0.1f, 0.1f, 0.9f, 1.85f, 0.9f);
            case EAST -> VoxelShapes.cuboid(0.3f, 0.1f, 0.1f, 0.9f, 1.85f, 0.9f);
            case WEST -> VoxelShapes.cuboid(0.3f, 0.1f, 0.1f, 0.9f, 1.85f, 0.9f);
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type){
        return false;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(
                    new MainMenu(Text.empty())
            );
        }
        return ActionResult.SUCCESS;
    }
}


