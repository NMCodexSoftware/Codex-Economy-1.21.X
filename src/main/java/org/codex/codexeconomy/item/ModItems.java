package org.codex.codexeconomy.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.codex.codexeconomy.CodexEconomy;
import org.codex.codexeconomy.item.custom.BankNote;
import org.codex.codexeconomy.item.custom.CreditCard;
import org.codex.codexeconomy.item.custom.DebitCard;

public class ModItems {
    // Items
    public static final Item RAW_DYSANIUM = registerItem("raw_dysanium", new Item(new Item.Settings()));
    public static final Item DYSANIUM_INGOT = registerItem("dysanium_ingot", new Item(new Item.Settings()));

    // Special Items
    public static final Item BANK_NOTE = registerItem("bank_note", new BankNote(new Item.Settings()));
    public static final Item CREDIT_CARD = registerItem("credit_card", new CreditCard(new Item.Settings()));
    public static final Item DEBIT_CARD = registerItem("debit_card", new DebitCard(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(CodexEconomy.MOD_ID, name), item);
    }

    public static void registerModItems() {
        CodexEconomy.LOGGER.info("Registering Mod Items for " + CodexEconomy.MOD_ID);
    }
}
