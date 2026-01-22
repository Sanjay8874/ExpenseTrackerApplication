# ExpenseTrackerApplication
A secure backend REST API for tracking personal expenses.
Users can register, login, add expenses, edit them, delete them, and view monthly/category-wise summaries.

#**🚀 Features**
✓ User Registration & Login (JWT Authentication)
✓ Add Expense
✓ Update Expense
✓ Delete Expense
✓ Fetch All Expenses
✓ Monthly & Category-wise Summary
✓ Input Validation
✓ Global Exception Handling
✓ Swagger/OpenAPI Documentation (if included)


**🧰 Tech Stack**
Category	Technology
Language	Java 17 (or your version)
Framework	Spring Boot
Security	Spring Security + JWT
Database	MySQL
ORM	Hibernate
Build Tool	Maven
Testing Tool	Postman
Documentation	Swagger/OpenAPI (optional)

**📁 Project Structure**
src/
 └─ main/
     ├─ java/
     │   └─ com/expense/expensetracker/
     │       ├─ Configuration
     |       ├─ controller
     |       ├─ DTO
     |       ├─ Entity
     |       ├─ Exception
     |       ├─ JWT
     |       ├─ Repository
     |       ├─ DTO
     │       ├─ Service
     │       ├─ ServiceImpl
     │       ├─ Validation
     │       └─ ExpenseTrackerApplication.java
     └─ resources/
         ├─ application.properties
         └─ schema.sql / data.sql (optional)

  #🗄️ Database Schema-



  #🔐 Authentication & Authorization

✓This project uses:
✓JWT tokens for login authentication
✓Spring Security for access control

Auth Flow
✓User registers via /auth/register
✓User logs in with /auth/login
✓Server returns JWT token

Token must be sent in Authorization header:
Authorization: Bearer <token>

========================================================================================================================

#⚙️ Setup & Installation
1. Clone the Repository:-
   git clone https://github.com/yourusername/expense-tracker.git

2. Configure MySQL
Create a database:
CREATE DATABASE expense_tracker;

3. Update application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=yourSecretKey

4. Run the Application
Using Maven:
mvn spring-boot:run







