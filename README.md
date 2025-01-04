# Online Shop Backend

## Overview
This project serves as the backend for an online shop platform, developed with **Spring Boot**. It offers key functionalities including product and category management, user registration, role-based access control, password reset via email, and image uploads. This backend is designed to work seamlessly with a frontend client (recommended: React) to deliver a complete e-commerce solution.

## Features
- **User Management**: User registration, role assignment, and password reset functionalities.
- **Product Management**: Adding, editing, and filtering products by price and category.
- **Category Management**: Adding and managing product categories.
- **JWT-based Security**: Role-based access control with JWT for authentication.
- **Image Upload**: Ability to upload and attach product images.
- **Email Service**: Sends password reset links to users..
- **Admin Features**: Allows admin users to assign roles and view activity logs.

## Technologies
- **Java 17** and **Spring Boot**
- **Spring Security** with JWT and role-based access control
- **Spring Data JPA** for data persistence
- **MySQL** as the database 
- **Jakarta Mail** for email functionality
- **MultipartFile** for file uploads
- **JSON Web Token (JWT)** for securing the API authentication
- **Lombok** (for simplifying Java classes)
- **JUnit & Mockito** for unit and integration testing
  
## Requirements
- Java 17+
- Maven 3+
- MySQL 
- Postman (for testing API endpoints)
- Frontend client (optional, recommended React)

## Installation and Setup

### Step 1: Clone the Repository
```
git clone https://github.com/your-repository/online-shop-backend.git
cd online-shop-backend
```

### Step 2: Configure Database
1.Edit ```application.properties``` under ```src/main/resources``` with your MySQL database credentials:

  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  ```
### Step 3: Set up Email for password reset
Configure email settings in ```application.properties``` to enable password reset:
```
  spring.mail.host=smtp.gmail.com
  spring.mail.port=587
  spring.mail.username=your-email@gmail.com
  spring.mail.password=your-email-password
  spring.mail.properties.mail.smtp.auth=true
  spring.mail.properties.mail.smtp.starttls.enable=true
```
### Step 4: Run the Application
Use Maven to build and run the project:
```
  mvn clean install
  mvn spring-boot:run
```
### Step 5: Access the api
 - The API will be available at http://localhost:8080.

## Usage
**1. User registration**
  - To register a new user, send a POST request to:
  ``` POST /api/auth/register ```
  -Request body (JSON):
  ```
  {
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "password123"
  }
```

**2. Password reset**
  -Request password reset via email:
  POST ``` /api/reset-password-request ```
  -Request body (JSON):
  ```
  {
  "email": "user@example.com"
  }
```

  -To reset the password, use the link sent via email:
  POST ``` /api/reset-password ```
  -Request body (JSON):
  ```
  {
  "token": "reset-token",
  "newPassword": "newPassword123"
  }
```

**3. Admin role assignment**
  -Admins can assign roles to other users:
  ``` POST /api/admin/assign-role ```
  -Request body (form data):
  ``` username=newuser ```
  ``` role=ADMIN ```
  
**4. Product Management**
  -Get all products:
  ``` GET /api/product/get ```

  -Add a new product (Admins only):
  ``` POST /api/product/save ```
  -Request body (form data):
  ``` name=ProductName ```
  ``` price=99.99 ```
  ``` quantity=10 ```
  ``` categoryName=CategoryName ```
  ``` file=imageFile.jpg ``` (optional)

  -Filter products by price range:
 ``` GET /api/product/filter-by-price?minPrice=10&maxPrice=50 ```
 
 **5. Category Management**
 - Add a category (Admins only):
   ``` POST /api/category/save ``` 

## API Security
  - **JWT Authentication**: Secure all endpoints (except for login, registration, and password reset) with JWT tokens.
  - **Role-Based Access Control**:Ensure role-specific access
    - **User**: Can browse products and manage their own account.
    - **Admin**: Can manage products, assign roles, and view users.
    - **Super Admin**: Has full access to all endpoints and can assign roles.

## Testing
- **Unit Tests**: The project includes JUnit and Mockito tests for services and controllers.
- **Run Tests**: Run all tests with the following Maven command:
  ```
  mvn test
  ```
- **Test API Endpoints**: Use Postman to interact with the API endpoints and verify responses.

## Troubleshooting
- **Database Errors**: Ensure your MySQL server is running, and the database credentials in ``` application.properties ``` are correct.
- **Email Not Sent**: Double-check your email settings and make sure less secure app access is enabled in Gmail for testing.
- **JWT Token Issues**: Verify token validity and check the ``` Authorization ``` header format (``` Bearer <token> ```).
