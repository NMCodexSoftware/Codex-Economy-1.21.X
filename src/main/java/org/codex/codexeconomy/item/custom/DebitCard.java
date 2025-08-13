package org.codex.codexeconomy.item.custom;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.codex.codexeconomy.block.ModBlocks;

public class DebitCard extends Item {
    public DebitCard(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if(clickedBlock == ModBlocks.ATM) {
            // Add Functionality
        }
        return super.useOnBlock(context);
    }
}
