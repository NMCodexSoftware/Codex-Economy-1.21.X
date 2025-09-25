package org.codex.codexeconomy.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.codex.codexeconomy.item.ModItems;


import java.util.List;

public class BankNote extends Item {
    public BankNote(Settings settings) {
        super(settings);
    }

    public static void setValue(ItemStack stack, double value) {
        NbtCompound tag = new NbtCompound();
        tag.putDouble("NoteValue", value);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }

    public static double getValue(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (comp != null) {
            NbtCompound tag = comp.copyNbt();
            if (tag.contains("NoteValue")) {
                return tag.getDouble("NoteValue");
            }
        }
        return 0.0;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Value: $" + getValue(stack)));
    }

    public void splitNote(PlayerEntity player, ItemStack stack, double amount) {
        double originalValue = getValue(stack);

        if (amount > 0 && amount < originalValue) {
            setValue(stack, originalValue - amount);

            ItemStack newNote = new ItemStack(ModItems.BANK_NOTE);
            setValue(newNote, amount);

            if (!player.getInventory().insertStack(newNote)) {
                player.dropItem(newNote, false);
            }
        }
    }
}
