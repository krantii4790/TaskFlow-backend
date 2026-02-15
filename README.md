## TaskFlow – Smart Daily Productivity Planner

A full-stack productivity management system built with React + Spring Boot + JWT Authentication, designed to manage daily tasks, schedules, and progress tracking with secure user authentication.
---

## 🚀 Overview

TaskFlow is a secure full-stack web application that allows users to:

✅ Create and manage daily todos

⏰ Plan activities with time scheduling

📊 Track daily and monthly progress

🔐 Authenticate securely using JWT

📈 View productivity insights on a dashboard

---

**This project demonstrates strong understanding of:**

- REST API design

- JWT-based authentication

- Spring Security

- React state management

- Full-stack integration

- Clean architecture
---

## 🏗️ Tech Stack
🔹 Frontend

React (Vite)

Axios

Recharts (Data Visualization)

Zustand (State Management)

Tailwind CSS

Lucide Icons

🔹 Backend

Spring Boot 3

Spring Security

JWT Authentication

Spring Data JPA

MySQL

Hibernate

Maven

---

## 🔐 Features

👤 Authentication

User Registration

Secure Login

JWT Token Generation

Token Validation via Filter

Protected Routes

📋 Todo Management

Create task

Update task status (PENDING / DONE)

Delete task

Fetch tasks by date

Fetch tasks by date range

⏰ Timetable Management

Schedule activities with start & end time

Fetch schedule by date

Fetch schedule by range

Delete entries

Clear daily timetable

📊 Dashboard & Progress

Daily task summary

Completion percentage

Monthly progress

Range-based analytics

---

## 📂 Project Structure
```
TaskFlow/
│
├── daily-planner-backend/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── dto/
│   ├── model/
│   └── config/
│
└── daily-planner-frontend/
    ├── components/
    ├── services/
    ├── store/
    └── pages/
```
---

**⚙️ Backend Setup**

1️⃣ Clone Repository

git clone https://github.com/krantii4790/TaskFlow.git

2️⃣ Configure Database (application.properties)
```
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/daily_planner
spring.datasource.username=root
spring.datasource.password=your_password
```

3️⃣ Run Backend

cd daily-planner-backend

mvn spring-boot:run


Backend runs on:

http://localhost:8080

**⚙️ Frontend Setup**

1️⃣ Install Dependencies

cd daily-planner-frontend

npm install

2️⃣ Run Frontend

npm run dev


Frontend runs on:

http://localhost:5173

---

## 🔄 API Design

🔹 Auth

POST /api/auth/register

POST /api/auth/login

🔹 Todos

GET    /api/todos

POST   /api/todos

PUT    /api/todos/{id}

DELETE /api/todos/{id}

GET    /api/todos/range

🔹 Timetable

GET    /api/timetable

POST   /api/timetable

DELETE /api/timetable/{id}

GET    /api/timetable/range

🔹 Progress

GET    /api/progress

GET    /api/progress/monthly

POST   /api/progress/calculate

🔹 Dashboard

GET /api/dashboard/summary

GET /api/dashboard/weekly

---

**🛡️ Security**

JWT-based stateless authentication

Spring Security filter chain

Authorization header validation

CORS configuration

Protected endpoints with @PreAuthorize

---

**🧠 Architecture Highlights**

Clean layered architecture (Controller → Service → Repository)

DTO pattern for API abstraction

Automatic progress recalculation

Proper exception handling

Interceptor logging in frontend

Reusable Axios instance

---

**🎯 What This Project Demonstrates**

Full-stack development capability

Secure authentication system

RESTful API implementation

Database design with relationships

Real-world productivity app logic

Clean, scalable project structure


**👨‍💻 Author**

Krantikumar Patil
