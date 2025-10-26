package com.kiranastore.dao;

import com.kiranastore.database.DatabaseConnection;
import com.kiranastore.model.Invoice;
import com.kiranastore.model.InvoiceItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceDAO.class);

    public int addInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoices (invoice_number, customer_name, customer_phone, total_items, " +
                     "subtotal, tax, total_amount, payment_method, invoice_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, invoice.getInvoiceNumber());
            pstmt.setString(2, invoice.getCustomerName());
            pstmt.setString(3, invoice.getCustomerPhone());
            pstmt.setInt(4, invoice.getTotalItems());
            pstmt.setDouble(5, invoice.getSubtotal());
            pstmt.setDouble(6, invoice.getTax());
            pstmt.setDouble(7, invoice.getTotalAmount());
            pstmt.setString(8, invoice.getPaymentMethod());
            pstmt.setTimestamp(9, Timestamp.valueOf(invoice.getInvoiceDate()));

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int invoiceId = rs.getInt(1);
                    addInvoiceItems(invoiceId, invoice.getItems());
                    logger.info("Invoice added: {}", invoice.getInvoiceNumber());
                    return invoiceId;
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding invoice", e);
        }
        return -1;
    }

    private void addInvoiceItems(int invoiceId, List<InvoiceItem> items) {
        String sql = "INSERT INTO invoice_items (invoice_id, product_id, product_name, quantity, unit_price, line_total) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (InvoiceItem item : items) {
                pstmt.setInt(1, invoiceId);
                pstmt.setInt(2, item.getProductId());
                pstmt.setString(3, item.getProductName());
                pstmt.setInt(4, item.getQuantity());
                pstmt.setDouble(5, item.getUnitPrice());
                pstmt.setDouble(6, item.getLineTotal());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            logger.error("Error adding invoice items", e);
        }
    }

    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    invoice.setItems(getInvoiceItems(id));
                    return invoice;
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching invoice by ID", e);
        }
        return null;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY invoice_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoice.setItems(getInvoiceItems(invoice.getId()));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            logger.error("Error fetching all invoices", e);
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByDate(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE DATE(invoice_date) BETWEEN ? AND ? ORDER BY invoice_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    invoice.setItems(getInvoiceItems(invoice.getId()));
                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching invoices by date", e);
        }
        return invoices;
    }

    private List<InvoiceItem> getInvoiceItems(int invoiceId) {
        List<InvoiceItem> items = new ArrayList<>();
        String sql = "SELECT * FROM invoice_items WHERE invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, invoiceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToInvoiceItem(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching invoice items", e);
        }
        return items;
    }

    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        return Invoice.builder()
            .id(rs.getInt("id"))
            .invoiceNumber(rs.getString("invoice_number"))
            .customerName(rs.getString("customer_name"))
            .customerPhone(rs.getString("customer_phone"))
            .totalItems(rs.getInt("total_items"))
            .subtotal(rs.getDouble("subtotal"))
            .tax(rs.getDouble("tax"))
            .totalAmount(rs.getDouble("total_amount"))
            .paymentMethod(rs.getString("payment_method"))
            .invoiceDate(rs.getTimestamp("invoice_date").toLocalDateTime())
            .build();
    }

    private InvoiceItem mapResultSetToInvoiceItem(ResultSet rs) throws SQLException {
        return InvoiceItem.builder()
            .id(rs.getInt("id"))
            .invoiceId(rs.getInt("invoice_id"))
            .productId(rs.getInt("product_id"))
            .productName(rs.getString("product_name"))
            .quantity(rs.getInt("quantity"))
            .unitPrice(rs.getDouble("unit_price"))
            .lineTotal(rs.getDouble("line_total"))
            .build();
    }
}
