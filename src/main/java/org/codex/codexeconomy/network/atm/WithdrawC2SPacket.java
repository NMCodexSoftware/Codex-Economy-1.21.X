package org.codex.codexeconomy.network.atm;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.codex.codexeconomy.network.NetworkHandler;

public record WithdrawC2SPacket(double amount) implements CustomPayload {
    public static final CustomPayload.Id<WithdrawC2SPacket> ID =
            new CustomPayload.Id<>(NetworkHandler.WITHDRAW);

    public static final PacketCodec<PacketByteBuf, WithdrawC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE, WithdrawC2SPacket::amount,
                    WithdrawC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
