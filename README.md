The Event Management System is a full-stack web application built with Spring Boot, MySQL, and HTML/CSS/JavaScript that enables users to discover, register for, and manage events.

The platform supports two authentication methods:

Google OAuth 2.0 Login (for quick sign-in)

Manual Registration & Login (with email & password)

Once logged in, users can:

View all available events.

Register for specific events.

Track how many events they have attended via a live Events Attended Counter on their dashboard.

The system stores user and event data in a MySQL database and uses Spring Security for authentication and authorization. The frontend is lightweight, responsive, and served from Spring Boot’s /static folder.

This project demonstrates secure authentication flows, role-based access, RESTful APIs, and database integration, making it a strong showcase for Java Full Stack Development skills.


📂 Project Structure
event-management/
 ├── src/main/java/com/eventmanagement/event_management/
 │   ├── config/        # Security Config, Data Loader
 │   ├── controller/    # User & Event Controllers
 │   ├── model/         # User, Event Entities
 │   ├── oauth/         # Custom OAuth2 User Service
 │   ├── repository/    # JPA Repositories
 │   ├── service/       # Service Layer
 │   └── EventManagementApplication.java
 ├── src/main/resources/
 │   ├── static/        # HTML, CSS, JS (Frontend)
 │   ├── templates/    
 │   └── application.properties
 └── pom.xml


Events

| Method | Endpoint                        | Description                |
| ------ | ------------------------------- | -------------------------- |
| GET    | `/api/events`                   | Get all events             |
| POST   | `/api/events`                   | Create a new event (Admin) |
| POST   | `/api/users/registerEvent/{id}` | Register user for event    |
| GET    | `/api/users/eventsAttended`     | Get events attended count  |




🔑 Endpoints

| Method | Endpoint              | Description                |
| ------ | --------------------- | -------------------------- |
| POST   | `/api/users/register` | Register new user          |
| POST   | `/api/users/login`    | Login user                 |
| GET    | `/api/users/me`       | Get current logged-in user |


# 🎯 Event Management System

A Spring Boot + HTML/CSS/JavaScript based Event Management System with:
- **User Registration** (Manual & Google OAuth 2.0)
- **User Login**
- **Events Listing & Registration**
- **Dashboard with Event Attendance Counter**
- **MySQL Database Integration**
- **Secure Authentication using Spring Security**

---

## 🚀 Features
- **Google OAuth 2.0 Login**
- **Manual Registration & Login**
- **Event Listing & Registration**
- **Events Attended Counter**
- **Secure API Access with Spring Security**
- **Responsive UI (HTML, CSS, JavaScript)**

---

## 🛠 Tech Stack
**Backend**  
- Java 21  
- Spring Boot 3.5  
- Spring Security (OAuth 2.0 Login)  
- Spring Data JPA (Hibernate)  
- MySQL Database  

**Frontend**  
- HTML, CSS, JavaScript  
- Static UI served from Spring Boot `/static` folder

---
⚙️ Setup Instructions
1️⃣ Clone Repository
 
git clone https://github.com/your-username/event-management.git
cd event-management

2️⃣ Configure Database
Create a MySQL database:
CREATE DATABASE event_db;

3️⃣ Add Environment Variables
Instead of hardcoding secrets in application.properties, use environment variables.
export GOOGLE_CLIENT_ID=your-google-client-id
export GOOGLE_CLIENT_SECRET=your-google-client-secret
export DB_USERNAME=root
export DB_PASSWORD=your-password

4️⃣ Configure application.properties
spring.application.name=event-management
spring.datasource.url=jdbc:mysql://localhost:3306/event_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
spring.security.oauth2.client.registration.google.scope=email,profile

5️⃣ Run Application
./mvnw spring-boot:run
