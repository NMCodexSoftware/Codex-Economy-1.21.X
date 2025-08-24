package org.codex.codexeconomy.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyState extends PersistentState {
    private final Map<UUID, Double> balances = new HashMap<>();
    public Map<UUID, Double> getAllBalances() {
        return balances;
    }

    public static final Type<EconomyState> TYPE = new Type<>(
            EconomyState::new,              // constructor (for new saves)
            EconomyState::createFromNbt,    // loader (for existing saves)
            null                            // no additional codec needed
    );

    public EconomyState() {}

    public static EconomyState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        EconomyState state = new EconomyState();
        NbtCompound balancesNbt = tag.getCompound("balances");
        for (String key : balancesNbt.getKeys()) {
            state.balances.put(UUID.fromString(key), balancesNbt.getDouble(key));
        }
        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound balancesNbt = new NbtCompound();
        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            balancesNbt.putDouble(entry.getKey().toString(), entry.getValue());
        }
        tag.put("balances", balancesNbt);
        return tag;
    }

    public void setBalance(UUID player, double balance) {
        balances.put(player, balance);
        this.markDirty();
    }

    public double getBalance(UUID player) {
        return balances.getOrDefault(player, 0.0);
    }

    // 🚀 Accessor method for getting the state
    public static EconomyState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TYPE, "economy_state");
    }
}
