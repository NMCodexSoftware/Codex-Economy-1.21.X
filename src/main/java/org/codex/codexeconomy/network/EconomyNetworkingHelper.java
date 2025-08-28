package org.codex.codexeconomy.network;

import net.minecraft.server.network.ServerPlayerEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.codex.codexeconomy.data.PlayerData;

public class EconomyNetworkingHelper {
    public static void syncPlayerData(ServerPlayerEntity player, PlayerData data) {
        PlayerDataSyncS2CPacket packet = PlayerDataSyncS2CPacket.from(data);
        ServerPlayNetworking.send(player, packet);
    }
}
