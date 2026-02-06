# Food Delivery Order Management API

![CI Status](https://github.com/yourusername/food-delivery-api/actions/workflows/ci.yml/badge.svg)
![CD Status](https://github.com/yourusername/food-delivery-api/actions/workflows/cd.yml/badge.svg)

A robust RESTful API for managing food delivery operations, built with Spring Boot 4 and Java 25. This system handles restaurant management, menu items, orders, and delivery tracking.

## 🚀 Features

### Currently Implemented
- **Restaurant Management**: Complete CRUD operations for restaurant entities
- **Menu Item Management**: Full menu management with categories and availability tracking
- **Input Validation**: Comprehensive validation for all incoming requests
- **Duplicate Prevention**: Database constraints and service-layer checks to prevent duplicate restaurants
- **Exception Handling**: Global exception handling with meaningful error messages
- **RESTful Design**: Following REST best practices with proper HTTP status codes
- **Resource Validation**: Ensures menu items belong to their associated restaurants
- **CI/CD Pipeline**: Automated testing and code quality checks with GitHub Actions

### Planned Features
- Customer management
- Order creation and management with status tracking
- Delivery assignment and real-time status updates
- Advanced search and filtering capabilities
- Order history and reporting
- Payment processing integration

## 🛠️ Tech Stack

- **Java**: 25
- **Spring Boot**: 4.0.2
- **Database**: H2 (embedded SQL database)
- **Build Tool**: Maven
- **CI/CD**: GitHub Actions
- **Dependencies**:
    - Spring Web
    - Spring Data JPA
    - Lombok
    - Jakarta Validation
    - H2 Database
    - JaCoCo (Code Coverage)

## 📋 Prerequisites

- JDK 25 or higher
- Maven 3.6+
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🔧 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/food-delivery-api.git
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

### Menu Item Management

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/restaurants/{restaurantId}/menu-items` | Get all menu items for a restaurant | - | 200 OK with list |
| GET | `/api/restaurants/{restaurantId}/menu-items?category=ENTREE` | Get menu items filtered by category | - | 200 OK with filtered list |
| GET | `/api/restaurants/{restaurantId}/menu-items/{id}` | Get specific menu item | - | 200 OK or 404 Not Found |
| POST | `/api/restaurants/{restaurantId}/menu-items` | Create menu item for restaurant | MenuItem JSON | 201 Created or 400/404 |
| PUT | `/api/restaurants/{restaurantId}/menu-items/{id}` | Update menu item | MenuItem JSON | 200 OK or 404 Not Found |
| DELETE | `/api/restaurants/{restaurantId}/menu-items/{id}` | Delete menu item | - | 204 No Content or 404 |

### Request/Response Examples

#### Create Restaurant
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
  "isActive": true,
  "createdAt": "2026-01-27T10:30:00"
}
```

#### Create Menu Item
```bash
POST http://localhost:8080/api/restaurants/1/menu-items
Content-Type: application/json

{
  "name": "Margherita Pizza",
  "description": "Classic pizza with tomato, mozzarella, and basil",
  "price": 12.99,
  "category": "ENTREE",
  "isAvailable": true
}
```

**Success Response (201 Created)**
```json
{
  "id": 1,
  "name": "Margherita Pizza",
  "description": "Classic pizza with tomato, mozzarella, and basil",
  "price": 12.99,
  "category": "ENTREE",
  "isAvailable": true,
  "restaurantId": 1,
  "restaurantName": "Pizza Palace",
  "createdAt": "2026-01-27T10:35:00",
  "updatedAt": "2026-01-27T10:35:00"
}
```

#### Get Menu Items by Category
```bash
GET http://localhost:8080/api/restaurants/1/menu-items?category=ENTREE
```

**Success Response (200 OK)**
```json
[
  {
    "id": 1,
    "name": "Margherita Pizza",
    "description": "Classic pizza with tomato, mozzarella, and basil",
    "price": 12.99,
    "category": "ENTREE",
    "isAvailable": true,
    "restaurantId": 1,
    "restaurantName": "Pizza Palace",
    "createdAt": "2026-01-27T10:35:00",
    "updatedAt": "2026-01-27T10:35:00"
  }
]
```

### Error Response Examples

**Validation Error (400 Bad Request)**
```json
{
  "timestamp": "2026-01-27T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "name: Restaurant name is required, cuisineType: Cuisine type is required",
  "path": "/api/restaurants"
}
```

**Invalid Category (400 Bad Request)**
```json
{
  "timestamp": "2026-02-05T13:30:00",
  "status": 400,
  "error": "Invalid Parameter",
  "message": "Invalid value for parameter 'category'. Valid options are: APPETIZER, ENTREE, DESSERT, BEVERAGE, SIDE, SPECIAL",
  "path": "/api/restaurants/1/menu-items"
}
```

**Resource Not Found (404)**
```json
{
  "timestamp": "2026-01-27T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Restaurant with id 999 does not exist",
  "path": "/api/restaurants/999"
}
```

**Menu Item Doesn't Belong to Restaurant (404)**
```json
{
  "timestamp": "2026-02-05T14:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Menu item with id 5 does not belong to restaurant 2",
  "path": "/api/restaurants/2/menu-items/5"
}
```

**Duplicate Resource (409 Conflict)**
```json
{
  "timestamp": "2026-01-27T10:30:00",
  "status": 409,
  "error": "Duplicate Resource",
  "message": "Restaurant with name 'Pizza Palace' at address '123 Main St, Dallas, TX' already exists",
  "path": "/api/restaurants"
}
```

## 🏗️ Project Structure

```
src/main/java/com/yourname/fooddeliveryapi/
├── controller/          # REST controllers
│   ├── RestaurantController.java
│   └── MenuItemController.java
├── entity/             # JPA entities
│   ├── Restaurant.java
│   ├── MenuItem.java
│   └── MenuCategory.java (enum)
├── repository/         # Data access layer
│   ├── RestaurantRepository.java
│   └── MenuItemRepository.java
├── service/            # Business logic
│   ├── RestaurantService.java
│   └── MenuItemService.java
├── dto/                # Data Transfer Objects
│   ├── MenuItemRequest.java
│   ├── MenuItemResponse.java
│   └── ApiMessageResponse.java
├── exception/          # Custom exceptions and handlers
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── InvalidEnumValueException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
└── FoodDeliveryApiApplication.java
```

## ✅ Validation Rules

### Restaurant Entity
- **name**: Required, cannot be blank
- **address**: Required, cannot be blank
- **phone**: Must match format `XXX-XXXX` or `XXXXXXXXXX`
- **cuisineType**: Required, cannot be blank
- **Uniqueness**: Name + Address combination must be unique

### Menu Item Entity
- **name**: Required, cannot be blank
- **price**: Required, must be greater than 0.01
- **category**: Required, must be valid MenuCategory (APPETIZER, ENTREE, DESSERT, BEVERAGE, SIDE, SPECIAL)
- **restaurant**: Required, must reference existing restaurant
- **Ownership**: Menu items can only be accessed/modified through their parent restaurant

## 🧪 Testing

You can test the API using:
- **Postman**: Import the endpoints and test each operation
- **cURL**: Command-line testing
- **H2 Console**: View database state in real-time

Example cURL commands:

**Create a restaurant:**
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

**Create a menu item:**
```bash
curl -X POST http://localhost:8080/api/restaurants/1/menu-items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Classic Burger",
    "description": "Beef patty with lettuce, tomato, and special sauce",
    "price": 9.99,
    "category": "ENTREE",
    "isAvailable": true
  }'
```

**Get menu items by category:**
```bash
curl http://localhost:8080/api/restaurants/1/menu-items?category=ENTREE
```

## 🔄 CI/CD Pipeline

This project uses GitHub Actions for continuous integration and deployment:

### CI (Continuous Integration)
Runs on every push and pull request:
- ✅ Builds the project
- ✅ Runs all tests
- ✅ Generates test reports
- ✅ Checks code coverage with JaCoCo
- ✅ Runs code quality checks

### CD (Continuous Deployment)
Runs on merge to main:
- ✅ Builds production-ready artifact
- 🚧 Prepared for Azure deployment (coming soon)

Workflow files: `.github/workflows/ci.yml` and `.github/workflows/cd.yml`

## 📈 Roadmap

- [x] Restaurant management
- [x] Menu item management
- [x] CI/CD pipeline
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

**Current Version**: 0.0.3 - Menu Items Module