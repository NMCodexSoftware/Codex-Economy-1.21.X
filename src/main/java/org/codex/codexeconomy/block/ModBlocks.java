package org.codex.codexeconomy.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.codex.codexeconomy.CodexEconomy;
import org.codex.codexeconomy.block.custom.ATM;

public class ModBlocks {
    public static final Block DYSANIUM_BLOCK = register("dysanium_block",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final Block DYSANIUM_ORE = register("dysanium_ore",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final Block DEEPSLATE_DYSANIUM_ORE = register("deepslate_dysanium_ore",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final org.codex.codexeconomy.block.custom.ATM ATM = register("atm",
            new ATM(AbstractBlock.Settings.create()
                    .strength(1.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)));

    private static <T extends Block> T register(String path, T block) {
        Registry.register(Registries.BLOCK, Identifier.of("codexeconomy", path), block);
        Registry.register(Registries.ITEM, Identifier.of("codexeconomy", path),
                new BlockItem(block, new Item.Settings()));
        return block;
    }

//    private static Block registerBlock(String name, Block block) {
//        registerBlockItem(name, block);
//        return Registry.register(Registries.BLOCK, Identifier.of(CodexEconomy.MOD_ID, name), block);
//    }

//    private static void registerBlockItem(String name, Block block) {
//        Registry.register(Registries.ITEM, Identifier.of(CodexEconomy.MOD_ID, name),
//                new BlockItem(block, new Item.Settings()));
//    }

    public static void registerModBlocks() {
        CodexEconomy.LOGGER.info("Registering Mod Blocks for " + CodexEconomy.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.DYSANIUM_BLOCK);
            entries.add(ModBlocks.DYSANIUM_ORE);
            entries.add(ModBlocks.DEEPSLATE_DYSANIUM_ORE);
            entries.add(ModBlocks.ATM);
        });
    }
}
