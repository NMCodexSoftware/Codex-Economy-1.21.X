package org.codex.codexeconomy.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.codex.codexeconomy.data.PlayerData;

public record PlayerDataSyncS2CPacket(double balance, int creditScore, double loanAmount) implements CustomPayload {
    public static final CustomPayload.Id<PlayerDataSyncS2CPacket> ID =
            new CustomPayload.Id<>(NetworkHandler.PLAYER_DATA_SYNC);

    public static final PacketCodec<PacketByteBuf, PlayerDataSyncS2CPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE, PlayerDataSyncS2CPacket::balance,
                    PacketCodecs.VAR_INT, PlayerDataSyncS2CPacket::creditScore,
                    PacketCodecs.DOUBLE, PlayerDataSyncS2CPacket::loanAmount,
                    PlayerDataSyncS2CPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static PlayerDataSyncS2CPacket from(PlayerData data) {
        return new PlayerDataSyncS2CPacket(data.getBalance(), data.getCreditScore(), data.getLoanAmount());
    }
}
