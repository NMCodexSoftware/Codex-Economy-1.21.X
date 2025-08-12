package org.codex.codexeconomy.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.codex.codexeconomy.CodexEconomy;
import org.codex.codexeconomy.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup CODEX_ECONOMY = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(CodexEconomy.MOD_ID, "codex_economy"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.CREDIT_CARD))
                .displayName(Text.translatable("itemgroup.codexeconomy.codex_economy"))
                .entries((displayContext, entries) -> {
                    // Add items
                    entries.add(ModItems.BANK_NOTE);
                    entries.add(ModItems.CREDIT_CARD);
                    entries.add(ModItems.DEBIT_CARD);
                    entries.add(ModItems.RAW_DYSANIUM);
                    entries.add(ModItems.DYSANIUM_INGOT);
                    // Add blocks
                    entries.add(ModBlocks.DYSANIUM_BLOCK);
                    entries.add(ModBlocks.DYSANIUM_ORE);
                    entries.add(ModBlocks.DEEPSLATE_DYSANIUM_ORE);
                    entries.add(ModBlocks.ATM);
            }).build());

    public static void registerItemGroups(){
        CodexEconomy.LOGGER.info("Registering Item Groups for " + CodexEconomy.MOD_ID);
    }
}
