package org.codex.codexeconomy.network.atm;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.codex.codexeconomy.network.NetworkHandler;

public record DepositC2SPacket(double amount) implements CustomPayload {
    public static final CustomPayload.Id<DepositC2SPacket> ID =
            new CustomPayload.Id<>(NetworkHandler.DEPOSIT);

    public static final PacketCodec<PacketByteBuf, DepositC2SPacket> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.DOUBLE, DepositC2SPacket::amount,
                    DepositC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}