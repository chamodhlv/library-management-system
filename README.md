# Library Management System

A full-stack library management system with JWT-based authentication, role-based access control, and a complete borrowing/returning workflow with automatic fine calculation. Built as a portfolio project to demonstrate production-shaped backend architecture (Spring Boot) paired with a modern React frontend.

## Features

- **Public catalog browsing** — anyone can search and view the book catalog without an account
- **Member registration & login** — JWT-based stateless authentication with BCrypt password hashing
- **Role-based access control** — two roles (`MEMBER`, `LIBRARIAN`) with distinct permissions enforced at the API level
- **Borrow & return workflow** — automatic due-date calculation (14-day loan period), copy-count tracking, and overdue fine calculation (Rs. 50/day)
- **Librarian dashboard** — full CRUD management of books, authors, and categories; member oversight with role promotion/demotion; library-wide view of all active borrows
- **Clean error handling** — consistent JSON error responses with correct HTTP status codes (404, 400, 401) instead of raw stack traces

## Tech Stack

**Backend**
- Java 21, Spring Boot 4.1
- Spring Data JPA (Hibernate) + PostgreSQL
- Spring Security with JWT (jjwt)
- Maven

**Frontend**
- React (Vite)
- Redux Toolkit
- React Router
- Tailwind CSS v4
- Axios

**Infrastructure**
- Docker (PostgreSQL container)

## Architecture

The backend follows a standard layered architecture:

```
Controller  →  Service  →  Repository  →  Database
     ↓             ↓
   DTOs      Business Logic
```

- **Entities** are never exposed directly through the API — all responses go through dedicated Request/Response DTOs.
- **Services** own all business logic (fine calculation, copy-count management, password hashing) and are wrapped in `@Transactional` where multiple database writes must succeed or fail together (e.g. borrowing a book updates both the book's available copies and creates a borrow record).
- **A global exception handler** (`@RestControllerAdvice`) converts exceptions into consistent JSON error responses, distinguishing between "not found" (404), "business rule violation" (400), and authentication failures (401).

### Entity Relationship Diagram

```
Author  ──┐
          ├──< book_authors >──┐
Category ─┘                    ├── Book ──< BorrowRecord >── Member
                                │
                book_categories ┘
```

- `Book` ↔ `Author` and `Book` ↔ `Category` are many-to-many relationships (implicit join tables managed by Hibernate).
- `BorrowRecord` is a full associative entity (not just a join table) since it carries its own data — borrow date, due date, return date, and fine information.

## Authentication & Authorization

- Authentication is stateless — every request must carry a JWT in the `Authorization: Bearer <token>` header.
- Passwords are hashed with BCrypt before storage; raw passwords are never persisted or logged.
- A custom `JwtAuthenticationFilter` intercepts every request, validates the token, and populates Spring Security's context so downstream role checks (`hasRole("LIBRARIAN")`) work automatically.
- Endpoint access is split three ways:
  - **Public** — browsing the catalog (`GET /api/books`, `/api/authors`, `/api/categories`)
  - **Authenticated (any role)** — borrowing/returning books, viewing personal borrow history
  - **LIBRARIAN only** — managing books/authors/categories, viewing/managing members, viewing library-wide borrow records

## Getting Started

### Prerequisites
- JDK 21+
- Node.js 18+
- Docker

### Backend Setup

1. Start PostgreSQL via Docker:
   ```bash
   docker compose up -d
   ```

2. Configure `src/main/resources/application.properties` (see `application.properties.example` if provided, or set your own DB credentials and JWT secret).

3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   npm install
   ```

2. Start the development server:
   ```bash
   npm run dev
   ```

   The app will be available at `http://localhost:5173`.

## API Overview

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET | `/api/books` | Public |
| GET | `/api/books/search?title=` | Public |
| POST/PUT/DELETE | `/api/books/**` | LIBRARIAN |
| GET | `/api/authors`, `/api/categories` | Public |
| POST/PUT/DELETE | `/api/authors/**`, `/api/categories/**` | LIBRARIAN |
| GET | `/api/members/me` | Authenticated |
| GET | `/api/members` | LIBRARIAN |
| PATCH | `/api/members/{id}/role` | LIBRARIAN |
| POST | `/api/borrow-records/borrow` | Authenticated |
| PATCH | `/api/borrow-records/{id}/return` | Authenticated |
| GET | `/api/borrow-records/member/{memberId}` | Authenticated |
| GET | `/api/borrow-records/currently-borrowed` | LIBRARIAN |

## Key Design Decisions

- **DTOs over exposing entities directly** — prevents circular JSON serialization (Book ↔ Author ↔ Book), hides sensitive fields (passwords never leave the backend, even hashed), and lets request/response shapes evolve independently of the database schema.
- **Custom exceptions over generic `RuntimeException`** — `ResourceNotFoundException` and `BusinessRuleException` map to distinct, correct HTTP status codes via a centralized `@RestControllerAdvice`, rather than every endpoint returning a generic 500.
- **`Member` implements `UserDetails` directly** — rather than maintaining a separate `User` entity, `Member` doubles as the Spring Security principal, keeping the auth model simpler for this project's scope.
- **Stateless JWT sessions** — no server-side session storage, which scales better across multiple server instances and matches how most modern APIs handle authentication.

## Known Limitations / Future Improvements

- No pagination on list endpoints (`GET /api/books`, `/api/members`) — acceptable at small scale, would need addressing for production use.
- No refresh token mechanism — tokens simply expire after 24 hours, requiring re-login.
- No email verification on registration.
- Librarians can accidentally lock themselves out of the "Manage Members" self-demotion/deletion at the backend level (mitigated in the frontend UI, not yet enforced by the API itself).

## License

This project was built for portfolio and educational purposes.
