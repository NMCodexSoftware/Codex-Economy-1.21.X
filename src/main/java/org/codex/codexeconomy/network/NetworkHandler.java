package org.codex.codexeconomy.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;
import org.codex.codexeconomy.network.atm.DepositC2SPacket;
import org.codex.codexeconomy.network.atm.WithdrawC2SPacket;

public class NetworkHandler {
    public static final Identifier PLAYER_DATA_SYNC = Identifier.of("codexeconomy", "player_data_sync");
    public static final Identifier WITHDRAW = Identifier.of("codexeconomy", "withdraw");
    public static final Identifier DEPOSIT = Identifier.of("codexeconomy", "deposit");

    public static void registerPayloads() {
        // S2C
        PayloadTypeRegistry.playS2C().register(PlayerDataSyncS2CPacket.ID, PlayerDataSyncS2CPacket.CODEC);
        // C2S
        PayloadTypeRegistry.playC2S().register(DepositC2SPacket.ID, DepositC2SPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(WithdrawC2SPacket.ID, WithdrawC2SPacket.CODEC);
    }
}