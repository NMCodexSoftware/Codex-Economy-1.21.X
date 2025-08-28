package org.codex.codexeconomy.client;

public class ClientEconomyData {
    private static double balance;
    private static int creditScore;
    private static double loanAmount;

    public static void setBalance(double b) { balance = b; }
    public static void setCreditScore(int s) { creditScore = s; }
    public static void setLoanAmount(double l) { loanAmount = l; }

    public static double getBalance() { return balance; }
    public static int getCreditScore() { return creditScore; }
    public static double getLoanAmount() { return loanAmount; }
}
