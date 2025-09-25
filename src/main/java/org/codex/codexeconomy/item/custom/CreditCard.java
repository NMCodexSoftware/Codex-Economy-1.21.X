package org.codex.codexeconomy.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.List;

public class CreditCard extends Item {
    public CreditCard(Settings settings) {
        super(settings);
    }

    // Card Number
    public static void setCreditCardNumber(ItemStack stack, String value) {
        NbtCompound tag = new NbtCompound();
        tag.putString("CreditCardNumber", value);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }

    public static String getCreditCardNumber(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (comp != null) {
            NbtCompound tag = comp.copyNbt();
            if (tag.contains("CreditCardNumber")) {
                return tag.getString("CreditCardNumber");
            }
        }
        return "0000-0000-0000-0000";
    }

    // CVV
    public static void setCVV(ItemStack stack, String value) {
        NbtCompound tag = new NbtCompound();
        tag.putString("CreditCardCVV", value);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }

    public static String getCVV(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (comp != null) {
            NbtCompound tag = comp.copyNbt();
            if (tag.contains("CreditCardCVV")) {
                return tag.getString("CreditCardCVV");
            }
        }
        return "000";
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Card Number: " + getCreditCardNumber(stack)));
        tooltip.add(Text.literal("CVV: " + getCVV(stack)));
    }
}
