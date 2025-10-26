# Backend

Part of the Intelli-Watt project.
See the other components:
- Admin Panel → https://github.com/Intelli-Watt/Admin-Panel
- Mobile App → https://github.com/Intelli-Watt/Mobile-App
- Firmware → https://github.com/Intelli-Watt/Firmware
- PCB → https://github.com/Intelli-Watt/PCB
- Docs → https://github.com/Intelli-Watt/Docs
- AI Model → https://github.com/Intelli-Watt/AI

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven (or use the included Maven wrapper)

### Steps
1. Navigate to the Backend directory:
   ```
   cd Backend
   ```

2. Run the application using Maven wrapper:
   ```
   ./mvnw spring-boot:run
   ```
   Or using Maven directly:
   ```
   mvn spring-boot:run
   ```

3. The application will start on http://localhost:8080

### Building the Application
To build a JAR file:
```
./mvnw clean package
```

Then run:
```
java -jar target/backend-0.0.1-SNAPSHOT.jar
```
