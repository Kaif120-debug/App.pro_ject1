package com.kiranastore.service;

import com.kiranastore.dao.SalesReportDAO;
import com.kiranastore.model.SalesReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class SalesReportService {
    private static final Logger logger = LoggerFactory.getLogger(SalesReportService.class);
    private final SalesReportDAO salesReportDAO = new SalesReportDAO();
    private final InvoiceService invoiceService = new InvoiceService();

    public void generateDailySalesReport(LocalDate date) {
        double dailySales = invoiceService.getDailySales(date);
        double dailyProfit = invoiceService.getDailyProfit(date);
        int transactionCount = invoiceService.getDailyTransactionCount(date);

        salesReportDAO.addOrUpdateDailySalesReport(date, dailySales, dailyProfit, transactionCount);
        logger.info("Daily sales report generated for: {}", date);
    }

    public void generateTodaySalesReport() {
        generateDailySalesReport(LocalDate.now());
    }

    public SalesReport getSalesReportForDate(LocalDate date) {
        SalesReport report = salesReportDAO.getSalesReportByDate(date);
        if (report == null) {
            generateDailySalesReport(date);
            report = salesReportDAO.getSalesReportByDate(date);
        }
        return report;
    }

    public List<SalesReport> getSalesReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesReportDAO.getSalesReportsByDateRange(startDate, endDate);
    }

    public List<SalesReport> getMonthlyReports(int year, int month) {
        return salesReportDAO.getMonthlyReports(year, month);
    }

    public List<SalesReport> getAllSalesReports() {
        return salesReportDAO.getAllSalesReports();
    }

    public double getTotalSalesForMonth(YearMonth yearMonth) {
        return getMonthlyReports(yearMonth.getYear(), yearMonth.getMonthValue()).stream()
            .mapToDouble(SalesReport::getTotalSales)
            .sum();
    }

    public double getTotalProfitForMonth(YearMonth yearMonth) {
        return getMonthlyReports(yearMonth.getYear(), yearMonth.getMonthValue()).stream()
            .mapToDouble(SalesReport::getTotalProfit)
            .sum();
    }

    public double getProfitMarginForMonth(YearMonth yearMonth) {
        double totalSales = getTotalSalesForMonth(yearMonth);
        double totalProfit = getTotalProfitForMonth(yearMonth);
        if (totalSales == 0) return 0;
        return (totalProfit / totalSales) * 100;
    }

    public double getMonthlyAverageTransactionValue(YearMonth yearMonth) {
        List<SalesReport> reports = getMonthlyReports(yearMonth.getYear(), yearMonth.getMonthValue());
        if (reports.isEmpty()) return 0;

        double totalSales = reports.stream().mapToDouble(SalesReport::getTotalSales).sum();
        int totalTransactions = reports.stream().mapToInt(SalesReport::getTotalTransactions).sum();

        if (totalTransactions == 0) return 0;
        return totalSales / totalTransactions;
    }

    public double getYearlySales(int year) {
        return getAllSalesReports().stream()
            .filter(report -> report.getSaleDate().getYear() == year)
            .mapToDouble(SalesReport::getTotalSales)
            .sum();
    }

    public double getYearlyProfit(int year) {
        return getAllSalesReports().stream()
            .filter(report -> report.getSaleDate().getYear() == year)
            .mapToDouble(SalesReport::getTotalProfit)
            .sum();
    }

    public SalesReport getTopSalesDay(LocalDate startDate, LocalDate endDate) {
        return getSalesReportsByDateRange(startDate, endDate).stream()
            .max((r1, r2) -> Double.compare(r1.getTotalSales(), r2.getTotalSales()))
            .orElse(null);
    }

    public SalesReport getTopProfitDay(LocalDate startDate, LocalDate endDate) {
        return getSalesReportsByDateRange(startDate, endDate).stream()
            .max((r1, r2) -> Double.compare(r1.getTotalProfit(), r2.getTotalProfit()))
            .orElse(null);
    }
}
