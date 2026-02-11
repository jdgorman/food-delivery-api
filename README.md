# Food Delivery Order Management API

![CI Status](https://github.com/jdgorman/food-delivery-api/actions/workflows/ci.yml/badge.svg)
![CD Status](https://github.com/jdgorman/food-delivery-api/actions/workflows/cd.yml/badge.svg)

A robust RESTful API for managing food delivery operations, built with Spring Boot 4 and Java 25. This system handles restaurant management, menu items, customer profiles, delivery addresses, orders, and delivery tracking.

## 🚀 Features

### Currently Implemented
- **Restaurant Management**: Complete CRUD operations for restaurant entities
- **Menu Item Management**: Full menu management with categories and availability tracking
- **Customer Management**: Customer registration and profile management
- **Delivery Address Management**: Multiple addresses per customer with default address support
- **Input Validation**: Comprehensive validation for all incoming requests
- **Duplicate Prevention**: Database constraints and service-layer checks
- **Exception Handling**: Global exception handling with meaningful error messages
- **RESTful Design**: Following REST best practices with proper HTTP status codes
- **Resource Validation**: Ensures nested resources belong to their parent entities
- **CI/CD Pipeline**: Automated testing and code quality checks with GitHub Actions

### Planned Features
- Order creation and management with status tracking
- Driver assignment and delivery tracking
- Real-time order status updates
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

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/api/restaurants` | Get all restaurants | 200 OK with list |
| GET | `/api/restaurants/{id}` | Get restaurant by ID | 200 OK or 404 |
| POST | `/api/restaurants` | Create new restaurant | 201 Created or 400/409 |
| PUT | `/api/restaurants/{id}` | Update restaurant | 200 OK or 404 |
| DELETE | `/api/restaurants/{id}` | Delete restaurant | 204 No Content or 404 |

### Menu Item Management

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/api/restaurants/{restaurantId}/menu-items` | Get all menu items for a restaurant | 200 OK with list |
| GET | `/api/restaurants/{restaurantId}/menu-items?category=ENTREE` | Get menu items filtered by category | 200 OK with filtered list |
| GET | `/api/restaurants/{restaurantId}/menu-items/{id}` | Get specific menu item | 200 OK or 404 |
| POST | `/api/restaurants/{restaurantId}/menu-items` | Create menu item for restaurant | 201 Created or 400/404 |
| PUT | `/api/restaurants/{restaurantId}/menu-items/{id}` | Update menu item | 200 OK or 404 |
| DELETE | `/api/restaurants/{restaurantId}/menu-items/{id}` | Delete menu item | 204 No Content or 404 |

### Customer Management

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/api/customers` | Get all customers | 200 OK with list |
| GET | `/api/customers/{id}` | Get customer by ID | 200 OK or 404 |
| POST | `/api/customers` | Create new customer | 201 Created or 400/409 |
| PUT | `/api/customers/{id}` | Update customer | 200 OK or 404/409 |
| DELETE | `/api/customers/{id}` | Delete customer | 204 No Content or 404 |

### Delivery Address Management

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/api/customers/{customerId}/addresses` | Get all addresses for customer | 200 OK with list |
| GET | `/api/customers/{customerId}/addresses/{addressId}` | Get specific address | 200 OK or 404 |
| POST | `/api/customers/{customerId}/addresses` | Create delivery address | 201 Created or 400/404 |
| PUT | `/api/customers/{customerId}/addresses/{addressId}` | Update delivery address | 200 OK or 404 |
| DELETE | `/api/customers/{customerId}/addresses/{addressId}` | Delete delivery address | 204 No Content or 404 |

## 📝 Request/Response Examples

### Restaurant Operations

#### Create Restaurant
```bash
POST http://localhost:8080/api/restaurants
Content-Type: application/json

{
  "name": "Pizza Palace",
  "address": "123 Main St, Dallas, TX",
  "phone": "423-555-1234",
  "cuisineType": "Italian"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Pizza Palace",
  "address": "123 Main St, Dallas, TX",
  "phone": "423-555-1234",
  "cuisineType": "Italian",
  "active": true,
  "createTimestamp": "2026-01-27T10:30:00"
}
```

### Menu Item Operations

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

**Response (201 Created):**
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
  "createTimestamp": "2026-01-27T10:35:00",
  "updateTimestamp": "2026-01-27T10:35:00"
}
```

#### Filter Menu Items by Category
```bash
GET http://localhost:8080/api/restaurants/1/menu-items?category=ENTREE
```

### Customer Operations

#### Create Customer
```bash
POST http://localhost:8080/api/customers
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "423-555-9999"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "423-555-9999",
  "addressCount": 0,
  "createTimestamp": "2026-02-07T15:20:00",
  "updateTimestamp": "2026-02-07T15:20:00"
}
```

#### Update Customer
```bash
PUT http://localhost:8080/api/customers/1
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phone": "423-555-8888"
}
```

### Delivery Address Operations

#### Create Delivery Address
```bash
POST http://localhost:8080/api/customers/1/addresses
Content-Type: application/json

{
  "label": "Home",
  "streetAddress": "123 Main St",
  "city": "Dallas",
  "state": "TX",
  "zipCode": "75001",
  "isDefault": true
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "customerId": 1,
  "customerName": "John Doe",
  "label": "Home",
  "streetAddress": "123 Main St",
  "city": "Dallas",
  "state": "TX",
  "zipCode": "75001",
  "isDefault": true,
  "createTimestamp": "2026-02-07T15:25:00",
  "updateTimestamp": "2026-02-07T15:25:00"
}
```

#### Get All Addresses for Customer
```bash
GET http://localhost:8080/api/customers/1/addresses
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "customerId": 1,
    "customerName": "John Doe",
    "label": "Home",
    "streetAddress": "123 Main St",
    "city": "Dallas",
    "state": "TX",
    "zipCode": "75001",
    "isDefault": true,
    "createTimestamp": "2026-02-07T15:25:00",
    "updateTimestamp": "2026-02-07T15:25:00"
  }
]
```

## ⚠️ Error Response Examples

### Validation Error (400)
```json
{
  "timestamp": "2026-01-27T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "firstName: First name is required, email: Email must be valid",
  "path": "/api/customers"
}
```

### Invalid Category (400)
```json
{
  "timestamp": "2026-02-05T13:30:00",
  "status": 400,
  "error": "Invalid Parameter",
  "message": "Invalid value for parameter 'category'. Valid options are: APPETIZER, ENTREE, DESSERT, BEVERAGE, SIDE, SPECIAL",
  "path": "/api/restaurants/1/menu-items"
}
```

### Resource Not Found (404)
```json
{
  "timestamp": "2026-01-27T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer with id 999 does not exist",
  "path": "/api/customers/999"
}
```

### Nested Resource Validation (404)
```json
{
  "timestamp": "2026-02-07T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Delivery address with id 5 does not belong to customer 1",
  "path": "/api/customers/1/addresses/5"
}
```

### Duplicate Resource (409)
```json
{
  "timestamp": "2026-02-07T15:30:00",
  "status": 409,
  "error": "Duplicate Resource",
  "message": "Customer with email 'john.doe@example.com' already exists",
  "path": "/api/customers"
}
```

## 🏗️ Project Structure

```
src/main/java/com/jdgorman/fooddeliveryapi/
├── controller/          # REST controllers
│   ├── RestaurantController.java
│   ├── MenuItemController.java
│   ├── CustomerController.java
│   └── DeliveryAddressController.java
├── entity/             # JPA entities
│   ├── Restaurant.java
│   ├── MenuItem.java
│   ├── MenuCategory.java (enum)
│   ├── Customer.java
│   └── DeliveryAddress.java
├── repository/         # Data access layer
│   ├── RestaurantRepository.java
│   ├── MenuItemRepository.java
│   ├── CustomerRepository.java
│   └── DeliveryAddressRepository.java
├── service/            # Business logic
│   ├── RestaurantService.java
│   ├── MenuItemService.java
│   ├── CustomerService.java
│   └── DeliveryAddressService.java
├── dto/                # Data Transfer Objects
│   ├── MenuItemRequest.java
│   ├── MenuItemResponse.java
│   ├── CustomerRequest.java
│   ├── CustomerResponse.java
│   ├── DeliveryAddressRequest.java
│   ├── DeliveryAddressResponse.java
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

### Customer Entity
- **firstName**: Required, cannot be blank
- **lastName**: Required, cannot be blank
- **email**: Required, must be valid email format, unique across all customers
- **phone**: Required, cannot be blank

### Delivery Address Entity
- **label**: Required (e.g., "Home", "Work")
- **streetAddress**: Required, cannot be blank
- **city**: Required, cannot be blank
- **state**: Required, cannot be blank
- **zipCode**: Required, cannot be blank
- **isDefault**: Boolean - only one address per customer can be default
- **Ownership**: Addresses can only be accessed/modified through their parent customer

## 🔗 Data Relationships

```
Restaurant (1) -----> (Many) MenuItem
Customer (1) -----> (Many) DeliveryAddress
Customer (1) -----> (Many) Order [Coming Soon]
```

## 🧪 Testing

You can test the API using:
- **Postman**: Import the endpoints and test each operation
- **cURL**: Command-line testing
- **H2 Console**: View database state in real-time

### Example Test Flow

1. **Create a restaurant**
2. **Add menu items** to the restaurant
3. **Create a customer**
4. **Add delivery addresses** to the customer (one default, others not)
5. **Update** an address to be the new default
6. **Verify** the previous default is now non-default
7. **Delete** resources and verify cascade behavior

Example cURL commands:

```bash
# Create a customer
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "423-555-9999"
  }'

# Add a delivery address
curl -X POST http://localhost:8080/api/customers/1/addresses \
  -H "Content-Type: application/json" \
  -d '{
    "label": "Home",
    "streetAddress": "123 Main St",
    "city": "Dallas",
    "state": "TX",
    "zipCode": "75001",
    "isDefault": true
  }'

# Get all addresses
curl http://localhost:8080/api/customers/1/addresses
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
- [x] Customer management
- [x] Delivery address management
- [x] CI/CD pipeline
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

**Current Version**: 0.0.4 - Customer Management Module