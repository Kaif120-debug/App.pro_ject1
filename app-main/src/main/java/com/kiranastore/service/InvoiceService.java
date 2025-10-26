package com.kiranastore.service;

import com.kiranastore.dao.InvoiceDAO;
import com.kiranastore.model.Invoice;
import com.kiranastore.model.InvoiceItem;
import com.kiranastore.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class InvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final ProductService productService = new ProductService();

    public int createInvoice(String customerName, String customerPhone, List<InvoiceItem> items, String paymentMethod) {
        if (items == null || items.isEmpty()) {
            logger.warn("Cannot create invoice with no items");
            return -1;
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setCustomerName(customerName != null ? customerName : "Walk-in Customer");
        invoice.setCustomerPhone(customerPhone);
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setPaymentMethod(paymentMethod);
        invoice.setItems(items);
        calculateInvoiceTotals(invoice);

        int invoiceId = invoiceDAO.addInvoice(invoice);

        if (invoiceId != -1) {
            updateProductStock(items);
            logger.info("Invoice created successfully with ID: {}", invoiceId);
        }

        return invoiceId;
    }

    public Invoice getInvoiceById(int id) {
        return invoiceDAO.getInvoiceById(id);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceDAO.getAllInvoices();
    }

    public List<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate) {
        return invoiceDAO.getInvoicesByDate(startDate, endDate);
    }

    public List<Invoice> getDailySalesInvoices(LocalDate date) {
        return getInvoicesByDate(date, date);
    }

    public double getDailySales(LocalDate date) {
        return getDailySalesInvoices(date).stream()
            .mapToDouble(Invoice::getTotalAmount)
            .sum();
    }

    public double getDailyProfit(LocalDate date) {
        List<Invoice> invoices = getDailySalesInvoices(date);
        double totalProfit = 0;

        for (Invoice invoice : invoices) {
            for (InvoiceItem item : invoice.getItems()) {
                Product product = productService.getProductById(item.getProductId());
                if (product != null) {
                    double profitPerUnit = product.getSellingPrice() - product.getBuyingPrice();
                    totalProfit += profitPerUnit * item.getQuantity();
                }
            }
        }

        return totalProfit;
    }

    public int getDailyTransactionCount(LocalDate date) {
        return getDailySalesInvoices(date).size();
    }

    private void calculateInvoiceTotals(Invoice invoice) {
        double subtotal = invoice.getItems().stream()
            .mapToDouble(InvoiceItem::getLineTotal)
            .sum();

        invoice.setSubtotal(subtotal);
        invoice.setTax(subtotal * 0.05);  // 5% tax
        invoice.setTotalAmount(subtotal + invoice.getTax());
        invoice.setTotalItems(invoice.getItems().stream()
            .mapToInt(InvoiceItem::getQuantity)
            .sum());
    }

    private void updateProductStock(List<InvoiceItem> items) {
        for (InvoiceItem item : items) {
            Product product = productService.getProductById(item.getProductId());
            if (product != null) {
                int newQuantity = product.getQuantityInStock() - item.getQuantity();
                productService.updateStock(item.getProductId(), newQuantity);
            }
        }
    }

    private String generateInvoiceNumber() {
        long timestamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "INV-" + timestamp + "-" + uuid;
    }

    public double getTotalRevenue() {
        return getAllInvoices().stream()
            .mapToDouble(Invoice::getTotalAmount)
            .sum();
    }

    public int getTotalInvoiceCount() {
        return getAllInvoices().size();
    }

    public double getAverageInvoiceValue() {
        List<Invoice> invoices = getAllInvoices();
        if (invoices.isEmpty()) return 0;
        return getTotalRevenue() / invoices.size();
    }
}
