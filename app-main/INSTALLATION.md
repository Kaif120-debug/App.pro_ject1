# Detailed Installation Guide

## Prerequisites Check

Before starting, verify you have these installed:

### 1. Java JDK 17+

**Windows:**

```cmd
java -version
javac -version
```

If not installed:

- Download from: https://www.oracle.com/java/technologies/downloads/
- Run installer
- Add Java to PATH (should be automatic)

**macOS:**

```bash
java -version
javac -version
```

Using Homebrew:

```bash
brew install openjdk@17
```

**Linux (Ubuntu/Debian):**

```bash
sudo apt update
sudo apt install openjdk-17-jdk openjdk-17-jre
java -version
```

### 2. Maven 3.8+

**Windows:**

- Download from: https://maven.apache.org/download.cgi
- Extract to `C:\Program Files\Apache\maven`
- Add to PATH environment variable
- Verify:
  ```cmd
  mvn -version
  ```

**macOS:**

```bash
# Using Homebrew
brew install maven
mvn -version
```

**Linux:**

```bash
sudo apt update
sudo apt install maven
mvn -version
```

---

## Installation Steps

### Step 1: Clone or Download Project

**Option A: Using Git**

```bash
git clone <repository-url>
cd kirana-store-manager
```

**Option B: Download ZIP**

1. Download project ZIP
2. Extract to your preferred location
3. Open terminal/command prompt in extracted folder

### Step 2: Build Project

```bash
# Clean previous builds
mvn clean

# Download dependencies and build
mvn install

# This will:
# - Download JavaFX 21.0.2
# - Download SQLite JDBC driver
# - Download other dependencies
# - Compile all source code
# - Run tests (if any)

# Expected time: 2-10 minutes (depending on internet speed)
```

**First build will be slower** - subsequent builds are faster.

### Step 3: Run Application

**Option A: Using Maven**

```bash
mvn javafx:run
```

**Option B: Run JAR file**

```bash
# First build the JAR
mvn clean package

# Then run it
java -jar target/KiranaStoreManager.jar
```

**Option C: Using IDE (IntelliJ)**

1. Open IntelliJ IDEA
2. File → Open
3. Select the `pom.xml` file
4. Wait for indexing to complete
5. Right-click `KiranaStoreApp.java`
6. Select "Run 'KiranaStoreApp.main()'"

**Option D: Using IDE (Eclipse)**

1. Open Eclipse
2. File → Import → Existing Maven Projects
3. Select project folder
4. Click "Import"
5. Right-click project → Run As → Java Application
6. Select `KiranaStoreApp`

---

## Troubleshooting Installation

### Problem: "mvn command not found"

**Windows:**

1. Download Maven from https://maven.apache.org/
2. Extract to `C:\Program Files\Apache\maven`
3. Set MAVEN_HOME:
   - Right-click "This PC" → Properties
   - Advanced system settings
   - Environment Variables
   - New → `MAVEN_HOME` = `C:\Program Files\Apache\maven`
4. Add to PATH:
   - Edit PATH variable
   - Add `%MAVEN_HOME%\bin`
5. Restart terminal and verify: `mvn -version`

**macOS:**

```bash
# If Homebrew installed Maven
/usr/local/bin/mvn -version

# Or reinstall
brew uninstall maven
brew install maven
```

**Linux:**

```bash
which mvn
# If not found
sudo apt install maven
```

### Problem: "Java version mismatch"

Check your Java version:

```bash
java -version
javac -version
```

Must show version 17 or higher.

**If Java 17+ not installed:**

**Windows:**

- Download JDK 17+ from Oracle website
- Run installer
- Verify: `java -version`

**macOS:**

```bash
brew install openjdk@17
# Then set as default
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

**Linux:**

```bash
sudo apt install openjdk-17-jdk
java -version
```

### Problem: "Cannot download dependencies"

This usually means internet connectivity issue:

1. Check internet connection
2. Clear Maven cache:
   ```bash
   rm -rf ~/.m2/repository
   ```
3. Try again:
   ```bash
   mvn clean install
   ```

If behind corporate firewall, configure Maven proxy:

- Edit `~/.m2/settings.xml`
- Add proxy configuration
- See: https://maven.apache.org/guides/mini/guide-proxies.html

### Problem: "Cannot find FXML files"

Ensure project structure is correct:

```
kirana-store-manager/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       ├── fxml/
│   │       └── css/
```

If missing, extract all project files properly.

### Problem: "Application starts but crashes"

Check error message:

1. Look at console output
2. Common issues:
   - Missing FXML files
   - Database file permission
   - Library mismatch

**Solution:**

```bash
mvn clean install
mvn javafx:run
```

### Problem: Database issues

If you get SQLite errors:

1. Delete `kiranastore.db` file
2. Restart application (it will recreate)

Or manually clear database:

```bash
# Windows
del kiranastore.db

# macOS/Linux
rm kiranastore.db
```

---

## Verify Installation

After setup, verify everything works:

### Step 1: Check Dependencies

```bash
mvn dependency:tree
```

Should show:

- `org.openjfx:javafx-*` 21.0.2
- `org.xerial:sqlite-jdbc` 3.44.0.0
- `org.projectlombok:lombok` 1.18.30

### Step 2: Check Build

```bash
mvn clean compile
```

Should complete without errors.

### Step 3: Run Application

```bash
mvn javafx:run
```

Should launch the Kirana Store Manager window.

---

## Environment Setup for IDE

### IntelliJ IDEA

1. **Open Project:**
   - File → Open
   - Select `pom.xml`
   - Click "Open as Project"

2. **Configure JDK:**
   - File → Project Structure
   - Project → SDK → Select JDK 17+
   - OK

3. **Run Configuration:**
   - Run → Edit Configurations
   - Add New → Application
   - Main class: `com.kiranastore.KiranaStoreApp`
   - Save and Run

4. **Build Project:**
   - Build → Build Project
   - Or Ctrl+F9 (Windows/Linux) / Cmd+F9 (macOS)

### Eclipse IDE

1. **Import Project:**
   - File → Import
   - Maven → Existing Maven Projects
   - Select project folder
   - Finish

2. **Configure JDK:**
   - Project → Properties
   - Java Build Path
   - JRE System Library → Edit
   - Select JDK 17+

3. **Run Configuration:**
   - Run → Run Configurations
   - New Java Application
   - Main class: `com.kiranastore.KiranaStoreApp`
   - Run

### VS Code

1. **Extensions needed:**
   - Extension Pack for Java
   - Maven for Java

2. **Open Folder:**
   - Open project folder

3. **Run:**
   - Terminal → New Terminal
   - `mvn javafx:run`

---

## Post-Installation Setup

### 1. Database Initialization

The database is created automatically on first run:

- Location: `kiranastore.db` (in project folder)
- Tables created automatically:
  - products
  - invoices
  - invoice_items
  - sales_reports
  - stock_movements

### 2. Create Backup

```bash
# Backup database
cp kiranastore.db kiranastore.db.backup

# Or on Windows
copy kiranastore.db kiranastore.db.backup
```

### 3. Add Sample Data (Optional)

Open the application and:

1. Go to Inventory
2. Click "Add Product"
3. Add some sample products for testing

---

## Production Deployment

For running on production servers:

### Build Executable JAR

```bash
mvn clean package
```

Creates: `target/KiranaStoreManager.jar`

### Run JAR

```bash
java -jar KiranaStoreManager.jar
```

### Create Launcher Script

**Windows (`run.bat`):**

```batch
@echo off
java -jar KiranaStoreManager.jar
pause
```

**Linux/macOS (`run.sh`):**

```bash
#!/bin/bash
java -jar KiranaStoreManager.jar
```

Make executable:

```bash
chmod +x run.sh
./run.sh
```

---

## Performance Optimization

### For Slower Systems

Edit `pom.xml` to reduce JavaFX features:

```xml
<!-- Add to dependencies -->
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.2</version>
</dependency>
```

### Increase Memory (if needed)

```bash
java -Xmx1024m -jar KiranaStoreManager.jar
```

- `-Xmx1024m` = Max 1GB RAM
- `-Xms512m` = Initial 512MB RAM

---

## Installation Summary Checklist

- [ ] Java 17+ installed
- [ ] Maven 3.8+ installed
- [ ] Project extracted/cloned
- [ ] `mvn clean install` completed successfully
- [ ] `mvn javafx:run` launches application
- [ ] Database created (`kiranastore.db` visible)
- [ ] Can navigate all menu tabs
- [ ] Can add a test product
- [ ] Can create a test invoice

---

## Getting Help

If you encounter issues:

1. **Check error messages** - they usually indicate the problem
2. **Review logs** - check console output
3. **Search online** - most issues have solutions
4. **Recreate database** - delete `.db` file and restart
5. **Clean rebuild** - `mvn clean install`

---

**Next Step:** Follow [QUICKSTART.md](QUICKSTART.md) for first-time usage guide.
