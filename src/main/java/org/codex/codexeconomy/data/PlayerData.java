package org.codex.codexeconomy.data;

import net.minecraft.nbt.NbtCompound;

public class PlayerData {
    private double balance;
    private int creditScore; // Example future property
    private double loanAmount; // Example future property

    public PlayerData() {
        this.balance = 0.00;
        this.creditScore = 600; // default value
        this.loanAmount = 0.00;
    }

    // --- Balance ---
    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }

    // --- Credit score ---
    public int getCreditScore() { return creditScore; }

    public void setCreditScore(int creditScore) { this.creditScore = creditScore; }

    // --- Loan amount ---
    public double getLoanAmount() { return loanAmount; }

    public void setLoanAmount(double loanAmount) { this.loanAmount = loanAmount; }

    // --- Serialization ---
    public NbtCompound toNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putDouble("balance", balance);
        tag.putInt("creditScore", creditScore);
        tag.putDouble("loanAmount", loanAmount);
        return tag;
    }

    public static PlayerData fromNbt(NbtCompound tag) {
        PlayerData data = new PlayerData();
        data.balance = tag.getDouble("balance");
        data.creditScore = tag.getInt("creditScore");
        data.loanAmount = tag.getDouble("loanAmount");
        return data;
    }
}
