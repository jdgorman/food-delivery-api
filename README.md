# Food Delivery Order Management API

![CI Status](https://github.com/jdgorman/food-delivery-api/actions/workflows/ci.yml/badge.svg)
![CD Status](https://github.com/jdgorman/food-delivery-api/actions/workflows/cd.yml/badge.svg)

A robust RESTful API for managing food delivery operations, built with Spring Boot 4 and Java 25. This system handles restaurant management, menu items, orders, and delivery tracking.

## 🚀 Features

### Currently Implemented
- **Restaurant Management**: Complete CRUD operations for restaurant entities
- **Input Validation**: Comprehensive validation for all incoming requests
- **Duplicate Prevention**: Service-layer checks to prevent duplicate restaurants
- **Exception Handling**: Global exception handling with meaningful error messages
- **RESTful Design**: Following REST best practices with proper HTTP status codes

### Planned Features
- Menu item management with categories and availability tracking
- Order creation and management with status tracking
- Delivery assignment and real-time status updates
- Search and filtering for restaurants and menu items
- Order history and reporting

## 🛠️ Tech Stack

- **Java**: 25
- **Spring Boot**: 4.0.2
- **Database**: H2 (embedded SQL database)
- **Build Tool**: Maven
- **Dependencies**:
    - Spring Web
    - Spring Data JPA
    - Lombok
    - Jakarta Validation
    - H2 Database

## 📋 Prerequisites

- JDK 25 or higher
- Maven 3.6+
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🔧 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/jdgorman/food-delivery-api.git
   cd food-delivery-api
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📊 Database

The application uses H2 in-memory database for development. Database is automatically created on startup.

**Access H2 Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:file:./data/fooddelivery`
- **Username**: `sa`
- **Password**: *(leave empty)*

## 🔌 API Endpoints

### Restaurant Management

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/restaurants` | Get all restaurants | - | 200 OK with list |
| GET | `/api/restaurants/{id}` | Get restaurant by ID | - | 200 OK or 404 Not Found |
| POST | `/api/restaurants` | Create new restaurant | Restaurant JSON | 201 Created or 400/409 |
| PUT | `/api/restaurants/{id}` | Update restaurant | Restaurant JSON | 200 OK or 404 Not Found |
| DELETE | `/api/restaurants/{id}` | Delete restaurant | - | 204 No Content or 404 |

### Request/Response Examples

**Create Restaurant**
```bash
POST http://localhost:8080/api/restaurants
Content-Type: application/json

{
  "name": "Pizza Palace",
  "address": "123 Main St, Dallas, TX",
  "phone": "555-1234",
  "cuisineType": "Italian"
}
```

**Success Response (201 Created)**
```json
{
  "id": 1,
  "name": "Pizza Palace",
  "address": "123 Main St, Dallas, TX",
  "phone": "555-1234",
  "cuisineType": "Italian",
  "active": true,
  "createDate": "2026-01-27T10:30:00"
}
```

**Validation Error Response (400 Bad Request)**
```json
{
  "timestamp": "2026-01-27T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "name: Restaurant name is required, cuisineType: Cuisine type is required",
  "path": "/api/restaurants"
}
```

**Duplicate Error Response (409 Conflict)**
```json
{
  "timestamp": "2026-01-27T10:30:00",
  "status": 409,
  "error": "Duplicate Resource",
  "message": "Restaurant with name 'Pizza Palace' at address '123 Main St, Dallas, TX' already exists",
  "path": "/api/restaurants"
}
```

**Not Found Response (404)**
```json
{
  "timestamp": "2026-01-27T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant with id 999 does not exist",
  "path": "/api/restaurants/999"
}
```

## 🏗️ Project Structure

```
src/main/java/com/yourname/fooddeliveryapi/
├── controller/          # REST controllers
│   └── RestaurantController.java
├── entity/             # JPA entities
│   └── Restaurant.java
├── exception/          # Custom exceptions and handlers
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
├── repository/         # Data access layer
│   └── RestaurantRepository.java
├── service/            # Business logic
│   └── RestaurantService.java
└── FoodDeliveryApiApplication.java
```

## ✅ Validation Rules

### Restaurant Entity
- **name**: Required, cannot be blank
- **address**: Required, cannot be blank
- **phone**: Must match a valid US phone number with optional country code `+1`, optional parentheses around the area code, and optional separators (dot, hyphen, or space). Examples: `+1 123-456-7890`, `(123) 456 7890`, `123-456-7890`, `123.456.7890`, `1234567890`
- **cuisineType**: Required, cannot be blank
- **Uniqueness**: Name + Address combination must be unique

## 🧪 Testing

You can test the API using:
- **Postman**: Import the endpoints and test each operation
- **cURL**: Command-line testing
- **H2 Console**: View database state in real-time

Example cURL command:
```bash
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Burger Joint",
    "address": "456 Oak Ave",
    "phone": "555-5678",
    "cuisineType": "American"
  }'
```

## 📈 Roadmap

- [ ] Menu item management
- [ ] Customer management
- [ ] Order creation and tracking
- [ ] Driver assignment and delivery tracking
- [ ] Search and filtering capabilities
- [ ] Reporting and analytics
- [ ] Integration tests
- [ ] API documentation with Swagger/OpenAPI
- [ ] Deployment to Azure

## 🤝 Contributing

This is a portfolio project, but suggestions and feedback are welcome!

## 📝 License

This project is open source and available for educational purposes.

## 👤 Author

**Your Name**
- GitHub: [@jdgorman](https://github.com/jdgorman)
- LinkedIn: [Joey Gorman](https://www.linkedin.com/in/joseph-gorman/)

---

**Current Version**: 0.0.1 - Restaurant Management Module