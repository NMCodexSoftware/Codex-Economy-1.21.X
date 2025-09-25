package org.codex.codexeconomy.rendering.screens.atm;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.codex.codexeconomy.client.ClientEconomyData;
import org.codex.codexeconomy.network.atm.DepositC2SPacket;

public class DepositScreen extends Screen {
    public DepositScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        // Amount Text Field
        TextFieldWidget amountWidget = new TextFieldWidget(textRenderer, this.width / 2 - 60, this.height / 2 - 20, 120, 20, Text.of("Enter Amount"));

        // Deposit Button
        ButtonWidget depositWidget = ButtonWidget.builder(Text.of("Deposit"), (btn) -> {
            try {
                double amount = Double.parseDouble(amountWidget.getText());
                double balance = ClientEconomyData.getCashBalance();
                if(balance >= amount) {
                    ClientPlayNetworking.send(new DepositC2SPacket(amount));
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
        this.addDrawableChild(depositWidget);
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
