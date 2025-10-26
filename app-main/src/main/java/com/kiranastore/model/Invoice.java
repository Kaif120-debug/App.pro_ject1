package com.kiranastore.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<InvoiceItem> items = new ArrayList<>();

    // Default constructor
    public Invoice() {
    }

    // Constructor with all fields
    public Invoice(int id, String invoiceNumber, String customerName, String customerPhone,
                   int totalItems, double subtotal, double tax, double totalAmount,
                   String paymentMethod, LocalDateTime invoiceDate, LocalDateTime createdAt) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.totalItems = totalItems;
        this.subtotal = subtotal;
        this.tax = tax;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.invoiceDate = invoiceDate;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    // Business methods
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Invoice invoice = (Invoice) obj;
        return id == invoice.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}