# 📚 Library Management System

A comprehensive Library Management System built with **Spring Boot 3**, **Java 21**, and **PostgreSQL**, supporting librarian and patron roles. This RESTful API allows librarians to manage books and users, and patrons to borrow and return books. Includes role-based access control and Swagger API documentation.

---

## 🚀 Features

- User authentication and role-based authorization (`LIBRARIAN` and `PATRON`)
- Book management (add, view, search, and details)
- Borrowing and returning of books
- Borrowing history (per-user and system-wide)
- Overdue book tracking
- Fully documented with Swagger/OpenAPI

---

## 🔧 Technologies Used

- Java 21
- Spring Boot 3
- Spring Security
- PostgreSQL
- Swagger / OpenAPI
- Maven / Gradle

---

## 📁 Endpoints Overview

> **Base URL:** `/books`

### 🧑‍🏫 Librarian Access Only

| Method | Endpoint              | Description                      |
|--------|-----------------------|----------------------------------|
| POST   | `/books`              | Add a new book                   |
| GET    | `/books/all-history`  | View all users' borrowing history |
| GET    | `/books/overdue`      | View overdue books               |

### 👤 Patron Access Only

| Method | Endpoint                 | Description              |
|--------|--------------------------|--------------------------|
| POST   | `/books/borrow/{bookId}` | Borrow a book by ID      |
| POST   | `/books/return/{bookId}` | Return a book by ID      |
| GET    | `/books/history`         | View personal borrowing history |

### 🔓 Shared Access (Librarian + Patron)

| Method | Endpoint                | Description                     |
|--------|-------------------------|---------------------------------|
| GET    | `/books`                | Get all books                   |
| GET    | `/books/{bookId}`       | Get details of a specific book |
| GET    | `/books/search?title=X` | Search books by title           |

---

## 🛡️ Role-Based Access

- **LIBRARIAN**
  - Can add, view, search, update, and delete books
  - Can view all borrowing records and overdue books

- **PATRON**
  - Can view and search books
  - Can borrow and return books
  - Can view personal borrowing history

---

## 🔗 Setup Instructions

### ✅ Prerequisites

- Java 21
- Maven or Gradle
- PostgreSQL

### 🛠️ Run the Project

```bash
# Clone the repository
git clone https://github.com/HKARATAS20/PatikaFinalCase.git
cd PatikaFinalCase

# Update application.properties with your PostgreSQL credentials

# Run the application
./mvnw spring-boot:run
