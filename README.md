# Online Shop Backend

## Overview
This is the backend of an online shop platform built using **Spring Boot**. The application provides functionalities for managing products, categories, users, and roles. It supports product listing, user registration, role management (e.g., assigning roles like Admin, Super Admin), and password reset functionality via email. The backend is designed to be used in combination with a frontend, providing a comprehensive e-commerce solution.

## Features
- **User Management**: User registration, role assignment, and password reset functionalities.
- **Product Management**: Adding, editing, and filtering products by price and category.
- **Category Management**: Adding and managing product categories.
- **Security**: JWT-based authentication with role-based access control (admin, super admin).
- **File Upload**: Ability to upload and attach product images.
- **Password Reset**: Users can request password resets via email.
- **Admin Role Management**: Admin users can assign roles to other users.

## Technologies
- **Java 17** and **Spring Boot**
- **Spring Security** with role-based authentication and authorization
- **Spring Data JPA** for database access
- **H2** or **MySQL** as the database (configurable via profiles)
- **Jakarta Mail** for sending email
- **MultipartFile** for file uploads
- **JSON Web Token (JWT)** for securing the API
- **Lombok** (for simplifying Java classes)
  
## Requirements
- Java 17+
- Maven 3+
- MySQL or H2 Database
- Postman (for testing API endpoints)
- Frontend client (optional, recommended React)

## Installation and Setup

### Step 1: Clone the Repository
```bash
git clone https://github.com/your-repository/online-shop-backend.git
cd online-shop-backend

**### Step 2: Configure Database#**
1. In the src/main/resources/application.properties, configure your database connection:

  spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
  spring.datasource.username=your_username
  spring.datasource.password=your_password

**###Step 3: Set up Email for password reset**
To enable password reset functionality, configure email settings in application.properties:

  spring.mail.host=smtp.gmail.com
  spring.mail.port=587
  spring.mail.username=your-email@gmail.com
  spring.mail.password=your-email-password
  spring.mail.properties.mail.smtp.auth=true
  spring.mail.properties.mail.smtp.starttls.enable=true

**###Step 4: Run the Application**
Use Maven to build and run the project:

  mvn clean install
  mvn spring-boot:run

**###Step 5: Access the api**
 - The API will be available at http://localhost:8080.

**##Usage**
**1. User registration**
  - To register a new user, send a POST request to:
  POST /api/auth/register
  -Request body (JSON):
  {
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123"
  }

**2. Password reset**
  -Request password reset via email:
  POST /api/reset-password-request
  -Request body (JSON):
  {
  "email": "user@example.com"
  }

  -To reset the password, use the link sent via email:
  POST /api/reset-password
  -Request body (JSON):
  {
  "token": "reset-token",
  "newPassword": "newPassword123"
  }

**3. Admin role assignment**
  -Admins can assign roles to other users:
  POST /api/admin/assign-role
  -Request body (form data):
  username=newuser
  role=ADMIN

**4. Product Management**
  -Get all products:
  GET /api/product/get

  -Add a new product (Admins only):
  POST /api/product/save
  -Request body (form data):
  name=ProductName
  price=99.99
  quantity=10
  categoryName=CategoryName
  file=imageFile.jpg (optional)

  -Filter products by price range:
  GET /api/product/filter-by-price?minPrice=10&maxPrice=50

**## API Security**
  -JWT Authentication: All endpoints (except login, registration, and password reset) are secured using JWT tokens.
  -Role-Based Access Control: Different roles (USER, ADMIN, SUPER_ADMIN) have different levels of access to the API.
    -User: Can browse products and manage their own account.
    -Admin: Can manage products, assign roles, and view users.
    -Super Admin: Has full access to all endpoints and can assign roles.

**##Testing**
  -Use Postman or Curl to test the API endpoints.
