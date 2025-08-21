# 📘 GamaLearn Automation

This project is a UI & Performance test automation framework built with Playwright & Gatling
, TestNG
, and Allure Reports
 to provide powerful reporting.
It is designed for cross-browser/device testing and supports passing runtime parameters.


## ⚙️ Tech Stack

- Java 17
- Maven (Build & Dependency Management)
- Playwright (UI Automation)
- TestNG (Test Runner & Suite Management)
- Allure TestNG (Reporting)
- Gatling (Performance)

## 📦 Dependencies

Key dependencies are defined in pom.xml:

``` <dependencies>
    <!-- Playwright -->
    <dependency>
        <groupId>com.microsoft.playwright</groupId>
        <artifactId>playwright</artifactId>
        <version>${playwright.version}</version>
    </dependency>

    <!-- TestNG -->
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>${testng.version}</version>
        <scope>test</scope>
    </dependency>

    <!-- Allure -->
    <dependency>
        <groupId>io.qameta.allure</groupId>
        <artifactId>allure-testng</artifactId>
        <version>${allure.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```



Plugins configured in Maven:
```
<build>
    <plugins>
        <!-- Test Runner -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
            <configuration>
                <suiteXmlFiles>
                    <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
                </suiteXmlFiles>
                <systemPropertyVariables>
                    <device>${device}</device>
                    <browser>${browser}</browser>
                </systemPropertyVariables>
            </configuration>
        </plugin>

        <!-- Allure Reporting -->
        <plugin>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-maven</artifactId>
            <version>2.12.0</version>
        </plugin>
    </plugins>
</build>
```

## ▶️ Running Tests

Run the entire suite:
```
mvn clean test
```
Run with TestNG suite file:
```
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml
```

## 🧩 Passing Parameters (Device / Browser)

Parameters can be passed at runtime using -D flags. These values are injected into the test via System.getProperty("paramName").
Examples:
```
# Run on Desktop Chrome
mvn clean test -Dbrowser=chromium -Ddevice=desktop

# Run on Mobile Safari
mvn clean test -Dbrowser=webkit -Ddevice=iPhone13
```

Inside your code you can access them like:
```
String browser = System.getProperty("browser", "chromium");
String device = System.getProperty("device", "desktop");
```

## 📊 Allure Reports
Generate & View Report

After running tests, Allure results will be stored in:
```
target/allure-results/
```

To run the maven and serve the report locally:
```
mvn clean test; allure serve target/allure-results
```

<img width="1680" height="630" alt="Screenshot 2025-08-21 at 3 07 12 PM" src="https://github.com/user-attachments/assets/d6b881e1-2d2e-410d-a059-b03bdee511ff" />
<img width="1502" height="830" alt="Screenshot 2025-08-21 at 3 07 26 PM" src="https://github.com/user-attachments/assets/01ee5cf2-85c5-4b02-b899-a1644645cabf" />


## 📷 Attachments & Logs

You can enhance Allure reports with screenshots and logs in your test code:
```
import io.qameta.allure.Attachment;

@Attachment(value = "Page screenshot", type = "image/png")
public byte[] saveScreenshot(byte[] screenshot) {
    return screenshot;
}
```

## Run Peformance:
```
mvn clean gatling:test
```
Report found under /target/gatling/.../index.html
<img width="1468" height="711" alt="Screenshot 2025-08-21 at 3 04 14 PM" src="https://github.com/user-attachments/assets/59ae6fba-5683-4c67-9944-e9d636719bc7" />
<img width="1014" height="703" alt="Screenshot 2025-08-21 at 3 04 24 PM" src="https://github.com/user-attachments/assets/475bd1cd-0bdd-40c7-85b6-12d01cadbd06" />
<img width="1010" height="752" alt="Screenshot 2025-08-21 at 3 04 33 PM" src="https://github.com/user-attachments/assets/8ffa8423-299e-4374-8f02-2aa3a109f2b3" />



## 📁 Project Structure
```
GamaLearn/
 ├── src/
 │   ├── main/java/...        # Test utilities / Base code
 │   └── test/java/...        # Test cases
 │   └── test/resources/      # TestNG XML suites
 ├── pom.xml                  # Maven dependencies & plugins
 └── README.md                # Project documentation
```
