# Retail Store Manager - Desktop Application

A production-ready JavaFX desktop application designed to manage operational inefficiencies for local Retail stores. The system provides comprehensive solutions for inventory management, billing, and sales reporting.

## Features

### 📊 Dashboard

- Real-time overview of key business metrics
- Sales trend visualization
- Low stock alerts and warnings
- Quick access to important statistics
- <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/2da74075-b21d-4324-9b4d-6fc974f61e8f" />


### 📦 Inventory Management

- Add, edit, and delete products
- Track product SKU, category, and pricing
- Monitor stock levels with reorder alerts
- Search and filter products by category
- Real-time inventory valuation
- <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/fcd31fc4-65d3-443a-9e71-0aafa07387bb" />


### 🧾 Billing System

- Quick and intuitive invoice creation
- Add multiple items to cart
- Real-time calculation of totals and taxes
- Multiple payment method support
- Invoice history and lookup
- <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/dd64eeb9-caac-4666-b65e-6431c82a7f44" />


### 📈 Sales Reports

- Comprehensive daily sales reporting
- Profit margin analysis
- Date range filtering
- Transaction count tracking
- Visualized sales trends and profit charts
- Monthly and yearly analytics
- <img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/5ac52001-b20e-45c7-a0ed-def57aed4cdc" />


## System Requirements

- **Java**: JDK 17 or higher
- **JavaFX**: 21.0.2 (included via Maven)
- **SQLite**: Database included
- **OS**: Windows, macOS, or Linux
- **RAM**: Minimum 512MB
- **Storage**: ~100MB free space

## Installation & Setup

### Option 1: Using Maven (Recommended)

1. **Clone or extract the project**

   ```bash
   cd kirana-store-manager
   ```

2. **Install dependencies**

   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Option 2: Build JAR and Run

1. **Build the project**

   ```bash
   mvn clean package
   ```

2. **Run the JAR**
   ```bash
   java -jar target/KiranaStoreManager.jar
   ```

### Option 3: IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Right-click on `KiranaStoreApp.java`
3. Select "Run 'KiranaStoreApp.main()'"

## Project Structure

```
kirana-store-manager/
├── src/main/
│   ├── java/com/kiranastore/
│   │   ├── KiranaStoreApp.java          # Application entry point
│   │   ├���─ controller/                  # JavaFX controllers
│   │   │   ├── MainDashboardController.java
│   │   │   ├── DashboardController.java
│   │   │   ├── InventoryController.java
│   │   │   ├── BillingController.java
│   │   │   └── ReportsController.java
│   │   ├── model/                       # Data models
│   │   │   ├── Product.java
│   │   │   ├── Invoice.java
│   │   │   ├── InvoiceItem.java
│   │   │   └── SalesReport.java
│   │   ├── service/                     # Business logic
│   │   │   ├── ProductService.java
│   │   │   ├── InvoiceService.java
│   │   │   └── SalesReportService.java
│   │   ├── dao/                         # Database access
│   │   │   ├── ProductDAO.java
│   │   │   ├── InvoiceDAO.java
│   │   │   └── SalesReportDAO.java
│   │   └── database/                    # Database configuration
│   │       ├── DatabaseConnection.java
│   │       └── DatabaseInitializer.java
│   ├── resources/
│   │   ├── fxml/                        # UI layouts
│   │   │   ├── MainDashboard.fxml
│   │   │   ├── Dashboard.fxml
│   │   │   ├── Inventory.fxml
│   │   │   ├── Billing.fxml
│   │   │   └── Reports.fxml
│   │   └── css/
│   │       └── styles.css               # Application styling
├── pom.xml                              # Maven configuration
└── README.md                            # This file
```

## Database

The application uses **MySQL** with the following tables:

- **products**: Store product information
- **invoices**: Store invoice records
- **invoice_items**: Store individual items in invoices
- **sales_reports**: Aggregated daily sales data
- **stock_movements**: Track inventory movements

Database file: `kiranastore.db` (created in the application directory)

## Usage Guide

### Adding a Product

1. Navigate to "Inventory" → Click "Add Product"
2. Enter product details (name, SKU, category, prices, quantity)
3. Click "OK" to save

### Creating an Invoice

1. Navigate to "Billing"
2. Select a product and enter quantity
3. Click "Add Item to Cart"
4. Enter customer details (optional)
5. Select payment method
6. Click "Generate Invoice"

### Viewing Reports

1. Navigate to "Reports"
2. Select date range using date pickers
3. Click "Generate Report"
4. View charts and statistics

## Features in Detail

### Inventory Management

- **Product Management**: Add, edit, delete products with full details
- **Stock Tracking**: Monitor quantity in stock with reorder levels
- **Low Stock Alerts**: Automatic alerts for low stock items
- **Inventory Valuation**: Calculate total inventory value in real-time
- **Category Organization**: Organize products by category

### Billing System

- **Quick Invoicing**: Create invoices quickly with multiple items
- **Real-time Calculation**: Automatic calculation of totals and taxes (5% default)
- **Multiple Payment Methods**: Support for cash, card, cheque, online transfer
- **Invoice History**: View all past invoices
- **Stock Integration**: Automatic stock deduction upon invoice generation

### Sales Reporting

- **Daily Reports**: Automatic daily sales report generation
- **Profit Analysis**: Track profit and profit margin
- **Sales Trends**: Visualize sales trends over time
- **Transaction Analytics**: Monitor transaction counts and average values
- **Period Comparison**: Compare sales across different date ranges

### Dashboard Analytics

- **Quick Stats**: View key business metrics at a glance
- **Sales Visualization**: Charts showing 7-day sales trend
- **Low Stock Warnings**: Priority alerts for products needing restock
- **Real-time Data**: All metrics updated in real-time

## Tax Calculation

The system applies a default **5% tax** on all invoices. To modify:

1. Open `InvoiceService.java`
2. Find the line: `invoice.setTax(subtotal * 0.05);`
3. Change `0.05` to your desired tax rate

## Default Settings

- **Tax Rate**: 5%
- **Database**: MySQL (Local file: `kiranastore.db`)
- **Date Format**: YYYY-MM-DD
- **Currency**: Indian Rupees (Rs.)

## Troubleshooting

### Issue: "SQLite JDBC driver not found"

**Solution**: Run `mvn clean install` to download dependencies

### Issue: "Cannot find FXML file"

**Solution**: Ensure `src/main/resources` is marked as a resource folder in your IDE

### Issue: Application fails to start

**Solution**:

- Check Java version: `java -version` (should be 17+)
- Clear Maven cache: `mvn clean`
- Rebuild project: `mvn install`

### Issue: Charts not displaying

**Solution**: Ensure JavaFX libraries are properly loaded. Run `mvn javafx:run`

## Performance Notes

- The application efficiently handles up to 100,000+ invoices
- Database queries are optimized for quick response
- UI updates are handled on the JavaFX thread to prevent freezing
- Stock calculations are cached for faster dashboard updates

## Future Enhancements

- Multi-user support with role-based access
- Cloud synchronization
- Barcode scanning integration
- Advanced analytics and forecasting
- Mobile app for remote access
- Export to PDF/Excel
- Backup and recovery options

## Security Notes

- Passwords and sensitive data are not currently stored (add authentication as needed)
- Database file should be backed up regularly
- Store database file in a secure location
- No data encryption is currently implemented (consider adding for production use)

## Support

For issues or questions:

1. Check the troubleshooting section above
2. Review the source code comments
3. Check the database schema in `DatabaseInitializer.java`

## License

This project is provided as-is for local Kirana store management.

## Author

Developed for efficient Kirana store operations management.

---

**Version**: 1.0.0  
**Last Updated**: 2024  
**Built with**: JavaFX 21, MySQL, Gradle
