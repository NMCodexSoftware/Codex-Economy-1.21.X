package org.codex.codexeconomy.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.List;

public class DebitCard extends Item {
    public DebitCard(Settings settings) {
        super(settings);
    }

    // Card Number
    public static void setDebitCardNumber(ItemStack stack, String value) {
        NbtCompound tag = new NbtCompound();
        tag.putString("DebitCardNumber", value);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }

    public static String getDebitCardNumber(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (comp != null) {
            NbtCompound tag = comp.copyNbt();
            if (tag.contains("DebitCardNumber")) {
                return tag.getString("DebitCardNumber");
            }
        }
        return "0000-0000-0000-0000";
    }

    // CVV
    public static void setCVV(ItemStack stack, String value) {
        NbtCompound tag = new NbtCompound();
        tag.putString("DebitCardCVV", value);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }

    public static String getCVV(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (comp != null) {
            NbtCompound tag = comp.copyNbt();
            if (tag.contains("DebitCardCVV")) {
                return tag.getString("DebitCardCVV");
            }
        }
        return "000";
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Card Number: " + getDebitCardNumber(stack)));
        tooltip.add(Text.literal("CVV: " + getCVV(stack)));
    }
}
