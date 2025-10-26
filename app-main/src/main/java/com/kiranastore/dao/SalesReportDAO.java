package com.kiranastore.dao;

import com.kiranastore.database.DatabaseConnection;
import com.kiranastore.model.SalesReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesReportDAO {
    private static final Logger logger = LoggerFactory.getLogger(SalesReportDAO.class);

    public void addOrUpdateDailySalesReport(LocalDate date, double totalSales, double totalProfit, int totalTransactions) {
        String selectSql = "SELECT id FROM sales_reports WHERE sale_date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setString(1, date.toString());
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    updateSalesReport(rs.getInt("id"), totalSales, totalProfit, totalTransactions);
                } else {
                    addSalesReport(date, totalSales, totalProfit, totalTransactions);
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding or updating sales report", e);
        }
    }

    private void addSalesReport(LocalDate date, double totalSales, double totalProfit, int totalTransactions) {
        String sql = "INSERT INTO sales_reports (sale_date, total_sales, total_profit, total_transactions) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, date.toString());
            pstmt.setDouble(2, totalSales);
            pstmt.setDouble(3, totalProfit);
            pstmt.setInt(4, totalTransactions);

            pstmt.executeUpdate();
            logger.info("Sales report added for date: {}", date);
        } catch (SQLException e) {
            logger.error("Error adding sales report", e);
        }
    }

    private void updateSalesReport(int id, double totalSales, double totalProfit, int totalTransactions) {
        String sql = "UPDATE sales_reports SET total_sales = ?, total_profit = ?, total_transactions = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, totalSales);
            pstmt.setDouble(2, totalProfit);
            pstmt.setInt(3, totalTransactions);
            pstmt.setInt(4, id);

            pstmt.executeUpdate();
            logger.info("Sales report updated for ID: {}", id);
        } catch (SQLException e) {
            logger.error("Error updating sales report", e);
        }
    }

    public SalesReport getSalesReportByDate(LocalDate date) {
        String sql = "SELECT * FROM sales_reports WHERE sale_date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, date.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSalesReport(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching sales report by date", e);
        }
        return null;
    }

    public List<SalesReport> getSalesReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<SalesReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM sales_reports WHERE sale_date BETWEEN ? AND ? ORDER BY sale_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToSalesReport(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching sales reports by date range", e);
        }
        return reports;
    }

    public List<SalesReport> getMonthlyReports(int year, int month) {
        List<SalesReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM sales_reports WHERE CAST(strftime('%Y', sale_date) AS INTEGER) = ? " +
                     "AND CAST(strftime('%m', sale_date) AS INTEGER) = ? ORDER BY sale_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToSalesReport(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching monthly reports", e);
        }
        return reports;
    }

    public List<SalesReport> getAllSalesReports() {
        List<SalesReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM sales_reports ORDER BY sale_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reports.add(mapResultSetToSalesReport(rs));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all sales reports", e);
        }
        return reports;
    }

    private SalesReport mapResultSetToSalesReport(ResultSet rs) throws SQLException {
        return SalesReport.builder()
            .id(rs.getInt("id"))
            .saleDate(rs.getDate("sale_date").toLocalDate())
            .totalSales(rs.getDouble("total_sales"))
            .totalProfit(rs.getDouble("total_profit"))
            .totalTransactions(rs.getInt("total_transactions"))
            .build();
    }
}
