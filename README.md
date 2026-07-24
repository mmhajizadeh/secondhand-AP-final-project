# Second Hand Market - Advanced Programming Final Project

A comprehensive full-stack client-server desktop application built with Spring Boot and JavaFX for buying and selling secondhand items.

---

## 👥 Team Members & Contribution Breakdown

As required by the course guidelines, the workload was distributed evenly (**50% - 50%**). Below is the comprehensive breakdown of our individual responsibilities:

### 🔹 Mohammad Mahdi Hajizadeh
* **Core Advertisement Lifecycle:** Designed and implemented the complete business logic for advertisement states (`PENDING`, `ACTIVE`, `SOLD`), along with ownership-based update and delete endpoints.
* **Image Management System:** Integrated a Base64 image encoding/decoding system supporting up to 3 images per listing (**Bonus Feature**) with dynamic gallery rendering on both ad cards and full detail views.
* **Advanced Search & In-Memory Sorting:** Built hybrid search algorithms filtering by text, category, city, and numeric price bounds, combined with real-time in-memory sorting (**Bonus Feature**) by *Newest*, *Oldest*, *Cheapest*, and *Most Expensive*.
* **UI & Frontend Architecture:** Designed the Main Dashboard (Grid Layout), Ad Details modal, My Ads panel, and Favorites view using JavaFX. Integrated `SceneManager` and `NavigationContext` to strictly enforce UI state and access permissions.

### 🔹 Amirhossein Hosseinpour
* **Authentication & Security Engine:** Developed JWT-based authentication from scratch, integrating `BCrypt` password hashing and establishing Stateless Security Filter Chains with role-based endpoint permissions.
* **Real-Time Communication Module:** Designed and built the internal chat system (`Conversations` & `Messages`), handling asynchronous message exchange between buyers and sellers with state checks for blocked users.
* **Reputation & Rating System:** Created the 1-to-5 star rating and comment framework for sellers, enforcing backend rules to block duplicate reviews and self-ratings.
* **Administrative Moderation Panel:** Implemented the Admin Dashboard empowering admins to moderate pending ads (Approve/Reject) and manage user statuses (Block/Unblock) with protective checks against self-blocking.

---

## 🧰 Technologies Used

### Backend
* **Java 21+ & Spring Boot 3.x / 4.x** (Spring Web MVC, Spring Data JPA, Spring Security)
* **SQLite & Hibernate** for persistent relational database storage
* **JSON Web Tokens (JJWT)** for stateless authentication
* **BCrypt Encoder** for password hashing

### Frontend
* **JavaFX 21+** (`javafx-controls`, `javafx-fxml`) following an MVC architecture
* **Jackson Databind** for JSON serialization/deserialization
* **Java Standard HTTP Client** (`java.net.http.HttpClient`) for REST API communication

---

## 📂 Repository Structure

```text
secondhand-AP-final-project/
├── .gitignore
├── README.md
├── secondhand.db
│
├── backend/
│   ├── pom.xml
│   ├── secondhand.db
│   ├── test-api.http
│   └── src/main/java/com/secondhand/backend/
│       ├── BackendApplication.java
│       ├── config/        # DataSeeder, SecurityConfig
│       ├── controller/    # REST API Controllers
│       ├── dto/           # Data Transfer Objects
│       ├── entity/        # JPA Entities (User, Advertisement, etc.)
│       ├── exception/     # Custom Exception Handling
│       ├── repository/    # JPA Repositories
│       ├── security/      # JWT Filters & Utilities
│       └── service/       # Service Layer Logic
│
└── frontend/
    ├── pom.xml
    └── src/main/
        ├── java/
        │   ├── module-info.java
        │   └── com/secondhand/frontend/
        │       ├── HelloApplication.java
        │       ├── Launcher.java
        │       ├── controller/  # JavaFX Controllers
        │       ├── model/       # DTO Models
        │       ├── service/     # API Client Services
        │       ├── session/     # Session Management
        │       └── util/        # Scene & Navigation Utilities
        └── resources/com/secondhand/frontend/
            ├── css/             # Application Styling
            └── view/            # FXML View Layouts
```

## 🚀 Implemented Features

### Base & Complete Features
* **User Authentication:** Secure JWT-based Login and Registration. Guest mode is supported for browsing.
* **Advertisement Lifecycle & Management:** Ads follow a strict cycle (`PENDING` -> `ACTIVE` -> `SOLD`). Owners can edit or delete their own ads.
* **Smart Search & Filtering:** Filter items dynamically based on category, location, text, and minimum/maximum price boundaries.
* **Direct Communication:** Built-in messaging system connecting buyers directly to ad owners.
* **Favorites System:** Users can save and manage their favorite advertisements.
* **Reputation System:** 1-5 star rating system for sellers after interactions.
* **Admin Controls:** Dedicated panel for user management (block/unblock) and ad moderation (approve/reject).

### Bonus Features Implemented
* **Multiple Images Gallery:** Support for uploading and viewing up to 3 images per advertisement.
* **Advanced Sorting:** Ability to sort search results by "Newest", "Cheapest", and "Most Expensive".

*(Note: Screenshots of the UI can be found in the `/screenshots` folder).*

---

## 💾 Data Storage

* **Database:** SQLite is used as the persistent storage mechanism.
* The database file (`secondhand.db`) is located in the root of the backend directory. It ensures that all users, ads, chats, and ratings remain intact across application restarts.

---

## ⚙️ Prerequisites & Execution Guide

### Prerequisites
* **Java Development Kit (JDK):** Version 21 or higher.
* **Build Tool:** Maven.
* **IDE:** IntelliJ IDEA (Recommended).

### 1. 1. Launching the Backend Server

```code
cd backend
mvn spring-boot:run
```

Or run BackendApplication.java via IntelliJ IDEA.
The server will start at http://localhost:8080. Database seeds automatically via DataSeeder.

### 2. Launching the Frontend Application (Client)

```code
cd frontend
mvn javafx:run
```

Or run HelloApplication.java via IntelliJ IDEA.

---

## 🔑 Test Accounts & Pre-seeded Data

To facilitate testing and evaluation, the database has been pre-seeded with sample cities, categories, and active advertisements. You can use the following test accounts to explore the system:

**1. Admin Account**
* **Username:** `admin1`
* **Password:** `123456`
* *Role:* Administrator (Access to Admin Dashboard, User Management, Ad Moderation).

**2. Standard User Account 1**
* **Username:** `user1`
* **Password:** `123456`
* *Role:* Regular User (Buyer/Seller). Has pre-existing ads and chats.

**3. Standard User Account 2**
* **Username:** `user2`
* **Password:** `123456`
* *Role:* Regular User (Buyer/Seller).

*You may also register a new user normally through the application's graphical interface.*
