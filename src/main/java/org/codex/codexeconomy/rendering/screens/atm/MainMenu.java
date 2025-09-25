package org.codex.codexeconomy.rendering.screens.atm;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.codex.codexeconomy.client.ClientEconomyData;

public class MainMenu extends Screen {
    public MainMenu(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        ButtonWidget depositWidget = ButtonWidget.builder(Text.of("Deposit"), (btn) -> {
            MinecraftClient.getInstance().setScreen(
                    new DepositScreen(Text.empty())
            );
        }).dimensions(this.width / 2 - 130, this.height / 2 - 20, 120, 20).build();
        ButtonWidget withdrawWidget = ButtonWidget.builder(Text.of("Withdraw"), (btn) -> {
            MinecraftClient.getInstance().setScreen(
                    new WithdrawScreen(Text.empty())
            );
        }).dimensions(this.width / 2 + 10, this.height / 2 - 20, 120, 20).build();
        ButtonWidget closeWidget = ButtonWidget.builder(Text.of("Close"), (btn) -> {
            MinecraftClient.getInstance().setScreen(null);
        }).dimensions(this.width / 2 - 60, this.height / 2 + 10, 120, 20).build();

        // Register the button widget.
        this.addDrawableChild(depositWidget);
        this.addDrawableChild(withdrawWidget);
        this.addDrawableChild(closeWidget);
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
