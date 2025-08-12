package org.codex.codexeconomy;

import net.fabricmc.api.ModInitializer;

import org.codex.codexeconomy.block.ModBlocks;
import org.codex.codexeconomy.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodexEconomy implements ModInitializer {
	public static final String MOD_ID = "codexeconomy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}