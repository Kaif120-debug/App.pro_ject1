package com.kiranastore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItem {
    private int id;
    private int invoiceId;
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double lineTotal;

    public void updateLineTotal() {
        this.lineTotal = quantity * unitPrice;
    }

    public static InvoiceItem create(Product product, int quantity) {
        return InvoiceItem.builder()
            .productId(product.getId())
            .productName(product.getName())
            .quantity(quantity)
            .unitPrice(product.getSellingPrice())
            .lineTotal(product.getSellingPrice() * quantity)
            .build();
    }

    @Override
    public String toString() {
        return productName + " x" + quantity + " @ Rs." + String.format("%.2f", unitPrice);
    }
}
