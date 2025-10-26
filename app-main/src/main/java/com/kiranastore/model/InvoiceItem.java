package com.kiranastore.model;

public class InvoiceItem {
    private int id;
    private int invoiceId;
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double lineTotal;

    // Default constructor
    public InvoiceItem() {
    }

    // Constructor with all fields
    public InvoiceItem(int id, int invoiceId, int productId, String productName,
                       int quantity, double unitPrice, double lineTotal) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    // Business methods
    public void updateLineTotal() {
        this.lineTotal = quantity * unitPrice;
    }

    public static InvoiceItem create(Product product, int quantity) {
        InvoiceItem item = new InvoiceItem();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setQuantity(quantity);
        item.setUnitPrice(product.getSellingPrice());
        item.setLineTotal(product.getSellingPrice() * quantity);
        return item;
    }

    @Override
    public String toString() {
        return productName + " x" + quantity + " @ Rs." + String.format("%.2f", unitPrice);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InvoiceItem that = (InvoiceItem) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}