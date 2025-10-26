package com.kiranastore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReport {
    private int id;
    private LocalDate saleDate;
    private double totalSales;
    private double totalProfit;
    private int totalTransactions;
    private LocalDateTime createdAt;

    public double getProfitMargin() {
        if (totalSales == 0) return 0;
        return (totalProfit / totalSales) * 100;
    }

    public double getAverageTransactionValue() {
        if (totalTransactions == 0) return 0;
        return totalSales / totalTransactions;
    }

    @Override
    public String toString() {
        return saleDate + " - Sales: Rs." + String.format("%.2f", totalSales);
    }
}
