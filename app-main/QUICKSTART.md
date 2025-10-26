# Kirana Store Manager - Quick Start Guide

## 5-Minute Setup

### Prerequisites

- Java 17 or higher installed
- Maven installed
- Git (optional)

### Step 1: Extract and Navigate

```bash
# If downloaded as ZIP, extract it
# Navigate to the project directory
cd kirana-store-manager
```

### Step 2: Install Dependencies

```bash
mvn clean install
```

This downloads all required libraries (JavaFX, SQLite, etc.)

### Step 3: Run the Application

```bash
mvn javafx:run
```

The application will start automatically!

---

## First-Time Setup Tasks

### 1. Add Your Products

- Click **📦 Inventory**
- Click **➕ Add Product**
- Fill in product details:
  - Product Name (e.g., "Rice - 5kg")
  - SKU (unique code, e.g., "RICE-5KG-001")
  - Category (e.g., "Grains")
  - Buying Price (e.g., 300)
  - Selling Price (e.g., 400)
  - Quantity (current stock)
  - Reorder Level (e.g., 10 units)
- Click OK

### 2. Create Your First Invoice

- Click **🧾 Billing**
- Select a product from dropdown
- Enter quantity (e.g., 2)
- Click "Add Item to Cart"
- (Optional) Enter customer name and phone
- Select payment method (Cash, Card, etc.)
- Click **✓ Generate Invoice**

The system automatically:

- Deducts stock from inventory
- Applies 5% tax
- Saves the invoice to database

### 3. View Reports

- Click **📈 Reports**
- Select a date range
- Click "Generate Report"
- View:
  - Total sales and profit
  - Sales charts
  - Daily transaction details

### 4. Check Dashboard

- Click **📊 Dashboard**
- View at a glance:
  - Total products and stock units
  - Inventory value
  - Today's sales
  - Low stock items
  - 7-day sales trend

---

## Common Tasks

### Adding Multiple Products Quickly

1. Go to Inventory
2. Click "Add Product"
3. Fill details and click OK
4. Repeat step 2-3 for each product

### Searching Products

- Use the search bar in Inventory to find by name or SKU
- Filter by category using the dropdown

### Editing a Product

1. Find the product in Inventory table
2. Click the product row
3. Click **✏️ Edit Product**
4. Update details and click OK

### Viewing Invoice History

1. Click **🧾 Billing**
2. Click "📋 View Invoices"
3. Browse all past invoices

### Generating Monthly Report

1. Click **📈 Reports**
2. Set date range (e.g., 1st to 30th of month)
3. Click "Generate Report"
4. View all statistics and charts

---

## Keyboard Shortcuts

| Action             | Shortcut           |
| ------------------ | ------------------ |
| Navigate Dashboard | Click 📊 Dashboard |
| Inventory          | Click 📦 Inventory |
| Billing            | Click 🧾 Billing   |
| Reports            | Click 📈 Reports   |
| Add Product        | Alt+P              |
| New Invoice        | Alt+I              |
| Generate Report    | Alt+R              |

---

## Default Values

- **Tax Rate**: 5% (can be modified in code)
- **Payment Methods**: Cash, Card, Cheque, Online Transfer
- **Currency**: Indian Rupees (Rs.)
- **Database**: SQLite local file

---

## Tips & Tricks

✅ **Inventory Management**

- Set reorder level to avoid running out of stock
- Review low stock alerts regularly
- Use SKU for quick product lookup

✅ **Billing**

- Add customer phone for CRM tracking
- Select correct payment method for accounting
- Check cart totals before generating invoice

✅ **Reports**

- Generate monthly reports for accounting
- Track profit margins to optimize pricing
- Monitor sales trends for business decisions

---

## Troubleshooting Startup Issues

### Issue: "Java not found"

```bash
# Check Java version
java -version

# If not installed:
# Download from: https://www.oracle.com/java/technologies/downloads/
```

### Issue: "Maven not found"

```bash
# Check Maven version
mvn -version

# If not installed:
# Download from: https://maven.apache.org/
```

### Issue: Application won't start

```bash
# Clean build
mvn clean

# Reinstall dependencies
mvn install

# Run again
mvn javafx:run
```

### Issue: "Cannot load FXML"

- Ensure you extracted all files (not just some)
- Check `src/main/resources/fxml/` folder exists
- Try: `mvn clean install` again

---

## Next Steps

After initial setup:

1. **Backup your database**: Copy `kiranastore.db` to a safe location
2. **Customize**: Modify default values in code as needed
3. **Train staff**: Have team members learn the interface
4. **Regular reporting**: Generate monthly reports for accounts

---

## Getting Help

If you encounter issues:

1. Check the **Troubleshooting** section above
2. Review `README.md` for detailed documentation
3. Check application logs for error messages
4. Ensure all prerequisites are installed correctly

---

## System Requirements Recap

✓ Java 17+  
✓ Maven (for running `mvn` commands)  
✓ ~100MB disk space  
✓ Windows, macOS, or Linux  
✓ No internet required after first setup

---

**Ready to go!** 🎉

Start managing your Kirana store efficiently with Kirana Store Manager.
