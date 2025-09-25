package org.codex.codexeconomy.data;

import net.minecraft.nbt.NbtCompound;

public class PlayerData {
    private double cashBalance;   // money in hand
    private double bankBalance;   // money in bank
    private int creditScore;
    private double loanAmount;

    public PlayerData() {
        this.cashBalance = 0.00;
        this.bankBalance = 0.00;
        this.creditScore = 600;
        this.loanAmount = 0.00;
    }

    // --- Cash ---
    public double getCashBalance() { return cashBalance; }
    public void setCashBalance(double balance) { this.cashBalance = balance; }

    // --- Bank ---
    public double getBankBalance() { return bankBalance; }
    public void setBankBalance(double balance) { this.bankBalance = balance; }

    // --- Credit score ---
    public int getCreditScore() { return creditScore; }
    public void setCreditScore(int creditScore) { this.creditScore = creditScore; }

    // --- Loan amount ---
    public double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(double loanAmount) { this.loanAmount = loanAmount; }

    // --- Serialization ---
    public NbtCompound toNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putDouble("cashBalance", cashBalance);
        tag.putDouble("bankBalance", bankBalance);
        tag.putInt("creditScore", creditScore);
        tag.putDouble("loanAmount", loanAmount);
        return tag;
    }

    public static PlayerData fromNbt(NbtCompound tag) {
        PlayerData data = new PlayerData();
        data.cashBalance = tag.getDouble("cashBalance");
        data.bankBalance = tag.getDouble("bankBalance");
        data.creditScore = tag.getInt("creditScore");
        data.loanAmount = tag.getDouble("loanAmount");
        return data;
    }
}