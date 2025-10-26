package com.kiranastore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private int id;
    private String name;
    private String sku;
    private String category;
    private double buyingPrice;
    private double sellingPrice;
    private int quantityInStock;
    private int reorderLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public double getProfit() {
        return sellingPrice - buyingPrice;
    }

    public double getProfitMargin() {
        if (sellingPrice == 0) return 0;
        return (getProfit() / sellingPrice) * 100;
    }

    public boolean isLowStock() {
        return quantityInStock <= reorderLevel;
    }

    @Override
    public String toString() {
        return name + " (SKU: " + sku + ")";
    }
}
