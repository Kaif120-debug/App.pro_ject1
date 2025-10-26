package com.kiranastore.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SalesReport {
    private int id;
    private LocalDate saleDate;
    private double totalSales;
    private double totalProfit;
    private int totalTransactions;
    private LocalDateTime createdAt;

    // Default constructor
    public SalesReport() {
    }

    // Constructor with all fields
    public SalesReport(int id, LocalDate saleDate, double totalSales, double totalProfit,
                       int totalTransactions, LocalDateTime createdAt) {
        this.id = id;
        this.saleDate = saleDate;
        this.totalSales = totalSales;
        this.totalProfit = totalProfit;
        this.totalTransactions = totalTransactions;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Business methods
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SalesReport that = (SalesReport) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}