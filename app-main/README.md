# Kirana Store Manager

A simple desktop application built with **Java + JavaFX + MySQL** for managing a Kirana store.

## Technology Stack

- **Java 21** - Programming language
- **JavaFX 21** - UI framework for desktop applications
- **MySQL 8.0** - Database for data persistence

## Features

- **Inventory Management**: Add, edit, delete products
- **Billing System**: Create invoices and manage sales
- **Dashboard**: View store statistics and reports
- **Database Integration**: All data stored in MySQL database

## Project Structure

```
src/main/java/com/kiranastore/
├── controller/          # JavaFX controllers
├── dao/                # Data Access Objects (Database operations)
├── database/           # Database connection and initialization
├── model/              # Data models (Product, Invoice, etc.)
├── service/            # Business logic services
└── KiranaStoreApp.java # Main application class
```

## Requirements

- Java 21 or higher
- MySQL 8.0 or higher
- JavaFX 21 (included in dependencies)

## Setup

1. **Install Java 21**
2. **Install MySQL and create database**
3. **Update database connection** in `DatabaseConnection.java`
4. **Run the application**:
   ```bash
   ./gradlew run
   ```

## Database Configuration

Update the database connection details in `src/main/java/com/kiranastore/database/DatabaseConnection.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/kirana_store";
private static final String DB_USER = "your_username";
private static final String DB_PASSWORD = "your_password";
```

## Building

```bash
# Build with Gradle
./gradlew build

# Run application
./gradlew run

# Create JAR file
./gradlew jar
```

## Features

- ✅ Product management (Add, Edit, Delete)
- ✅ Inventory tracking
- ✅ Billing and invoicing
- ✅ Sales reports
- ✅ Database persistence
- ✅ Modern JavaFX UI

## License

This project is for educational purposes.