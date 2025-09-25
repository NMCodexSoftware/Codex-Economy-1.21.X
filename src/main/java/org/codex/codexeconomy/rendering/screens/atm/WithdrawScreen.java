package org.codex.codexeconomy.rendering.screens.atm;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.codex.codexeconomy.client.ClientEconomyData;
import org.codex.codexeconomy.network.atm.WithdrawC2SPacket;

public class WithdrawScreen extends Screen {
    public WithdrawScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        // Amount Text Field
        TextFieldWidget amountWidget = new TextFieldWidget(textRenderer, this.width / 2 - 60, this.height / 2 - 20, 120, 20, Text.of("Enter Amount"));

        // Withdraw Button
        ButtonWidget withdrawWidget = ButtonWidget.builder(Text.of("Withdraw"), (btn) -> {
            try {
                double amount = Double.parseDouble(amountWidget.getText());
                double bankBalance = ClientEconomyData.getBankBalance();
                if (bankBalance >= amount) {
                    ClientPlayNetworking.send(new WithdrawC2SPacket(amount));
                }
            } catch (NumberFormatException e) {
                // Invalid input, ignore or show a warning
            }
        }).dimensions(this.width / 2 - 60, this.height / 2 + 10, 120, 20).build();

        // Main Menu Button
        ButtonWidget menuWidget = ButtonWidget.builder(Text.of("Main Menu"), (btn) -> {
            MinecraftClient.getInstance().setScreen(
                    new MainMenu(Text.empty())
            );
        }).dimensions(this.width / 2 - 60, this.height / 2 + 40, 120, 20).build();

        // Register Widgets
        this.addDrawableChild(amountWidget);
        this.addDrawableChild(withdrawWidget);
        this.addDrawableChild(menuWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        double balance = ClientEconomyData.getCashBalance();
        double bankBalance = ClientEconomyData.getBankBalance();
        context.drawText(this.textRenderer, "Cash: $" + balance, this.width / 2 - 50, this.height / 2 - 50, 0xFFFFFFFF, true);
        context.drawText(this.textRenderer, "Bank Balance: $" + bankBalance, this.width / 2 - 50, this.height / 2 - 40, 0xFFFFFFFF, true);
    }
}