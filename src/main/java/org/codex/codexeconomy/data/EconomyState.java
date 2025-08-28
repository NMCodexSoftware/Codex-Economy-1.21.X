package org.codex.codexeconomy.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import org.codex.codexeconomy.network.EconomyNetworkingHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyState extends PersistentState {
    private final Map<UUID, PlayerData> players = new HashMap<>();

    public static final Type<EconomyState> TYPE = new Type<>(
            EconomyState::new,
            EconomyState::createFromNbt,
            null
    );

    public EconomyState() {}

    public static EconomyState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        EconomyState state = new EconomyState();
        NbtCompound playersNbt = tag.getCompound("players");
        for (String key : playersNbt.getKeys()) {
            UUID uuid = UUID.fromString(key);
            NbtCompound playerTag = playersNbt.getCompound(key);
            state.players.put(uuid, PlayerData.fromNbt(playerTag));
        }
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound playersNbt = new NbtCompound();
        for (Map.Entry<UUID, PlayerData> entry : players.entrySet()) {
            playersNbt.put(entry.getKey().toString(), entry.getValue().toNbt());
        }
        tag.put("players", playersNbt);
        return tag;
    }

    // --- Helpers ---
    public PlayerData getOrCreate(UUID player) {
        return players.computeIfAbsent(player, id -> new PlayerData());
    }

    // --- Balance ---
    public void setBalance(UUID player, double balance) {
        getOrCreate(player).setBalance(balance);
        this.markDirty();
    }

    public void setBalance(UUID player, double balance, ServerPlayerEntity playerEntity) {
        PlayerData data = getOrCreate(player);
        data.setBalance(balance);
        this.markDirty();
        EconomyNetworkingHelper.syncPlayerData(playerEntity, data);
    }

    public double getBalance(UUID player) {
        return getOrCreate(player).getBalance();
    }

    // --- Credit score ---
    public void setCreditScore(UUID player, int score) {
        getOrCreate(player).setCreditScore(score);
        this.markDirty();
    }

    public void setCreditScore(UUID player, int score, ServerPlayerEntity playerEntity) {
        PlayerData data = getOrCreate(player);
        data.setCreditScore(score);
        this.markDirty();
        EconomyNetworkingHelper.syncPlayerData(playerEntity, data);
    }

    public int getCreditScore(UUID player) {
        return getOrCreate(player).getCreditScore();
    }

    // --- Loan amount ---
    public void setLoanAmount(UUID player, double amount) {
        getOrCreate(player).setLoanAmount(amount);
        this.markDirty();
    }

    public void setLoanAmount(UUID player, double amount, ServerPlayerEntity playerEntity) {
        PlayerData data = getOrCreate(player);
        data.setLoanAmount(amount);
        this.markDirty();
        EconomyNetworkingHelper.syncPlayerData(playerEntity, data);
    }

    public double getLoanAmount(UUID player) {
        return getOrCreate(player).getLoanAmount();
    }

    // --- Accessor ---
    public static EconomyState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TYPE, "economy_state");
    }
}
