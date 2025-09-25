package org.codex.codexeconomy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.codex.codexeconomy.client.ClientEconomyData;
import org.codex.codexeconomy.network.PlayerDataSyncS2CPacket;

public class CodexEconomyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Handle packet reception
        ClientPlayNetworking.registerGlobalReceiver(PlayerDataSyncS2CPacket.ID, (packet, context) -> {
            // Save to a client-only copy
            ClientEconomyData.setCashBalance(packet.cashBalance());
            ClientEconomyData.setBankBalance(packet.bankBalance());
            ClientEconomyData.setCreditScore(packet.creditScore());
            ClientEconomyData.setLoanAmount(packet.loanAmount());
        });
    }
}
