package org.codex.codexeconomy.client;

public class ClientEconomyData {
    private static double cashBalance;
    private static double bankBalance;
    private static int creditScore;
    private static double loanAmount;

    // --- Setters ---
    public static void setCashBalance(double amount) { cashBalance = amount; }
    public static void setBankBalance(double amount) { bankBalance = amount; }
    public static void setCreditScore(int score) { creditScore = score; }
    public static void setLoanAmount(double amount) { loanAmount = amount; }

    // --- Getters ---
    public static double getCashBalance() { return cashBalance; }
    public static double getBankBalance() { return bankBalance; }
    public static int getCreditScore() { return creditScore; }
    public static double getLoanAmount() { return loanAmount; }

    // --- Reset (optional helper for logout / reload) ---
    public static void reset() {
        cashBalance = 0.0;
        bankBalance = 0.0;
        creditScore = 600; // default
        loanAmount = 0.0;
    }
}