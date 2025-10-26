package com.kiranastore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    private int id;
    private String invoiceNumber;
    private String customerName;
    private String customerPhone;
    private int totalItems;
    private double subtotal;
    private double tax;
    private double totalAmount;
    private String paymentMethod;
    private LocalDateTime invoiceDate;
    private LocalDateTime createdAt;

    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    public void addItem(InvoiceItem item) {
        items.add(item);
        recalculate();
    }

    public void removeItem(InvoiceItem item) {
        items.remove(item);
        recalculate();
    }

    private void recalculate() {
        subtotal = items.stream().mapToDouble(InvoiceItem::getLineTotal).sum();
        totalItems = items.stream().mapToInt(InvoiceItem::getQuantity).sum();
        tax = subtotal * 0.05;  // 5% tax
        totalAmount = subtotal + tax;
    }

    @Override
    public String toString() {
        return "Invoice #" + invoiceNumber + " - Rs." + String.format("%.2f", totalAmount);
    }
}
