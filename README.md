# Second Hand Market - Advanced Programming Final Project

A comprehensive client-server application built with Spring Boot and JavaFX for buying and selling second-hand items. This project implements a real-world architecture separating the Backend logic and RESTful communication from the Frontend user interface, with data persistently stored using SQLite.

**Team Members:** Mohammad Mahdi Hajizadeh & Amirhossein Hosseinpour

---

## 👥 Team Members & Contributions

As required by the project guidelines, the workload was distributed evenly (50-50). Below is the breakdown of our responsibilities encompassing design, implementation, testing, and documentation:

### Mohammad Mahdi Hajizadeh
In this project, I was primarily responsible for the core business logic of advertisements and the overall user interface design. I designed and implemented the complete lifecycle of an advertisement (`PENDING`, `ACTIVE`, `SOLD`), along with the ability for users to edit or delete their own ads. To enhance the user experience, I implemented a robust image upload system capable of handling up to 3 images per ad (Bonus Feature) using Base64 encoding. Additionally, I developed the advanced search and filtering system, allowing users to filter ads by category, city, and strict numeric price boundaries, as well as sorting them by newest, cheapest, or most expensive (Bonus Feature).

On the frontend, I designed the Main Dashboard (Grid layout), Ad Details UI, and the Favorites system. I also established a smart `SceneManager` to seamlessly handle window navigation and strictly enforced UI constraints (e.g., hiding action buttons for non-active ads or guest users) to ensure the frontend accurately reflects backend security policies.

### Amirhossein Hosseinpour
My primary focus was on the security, authentication, communication, and administrative aspects of the application. I built the registration and login flows from scratch, securing the system with JWT authentication and BCrypt password hashing. I also designed and implemented the real-time chat engine, allowing buyers and sellers to communicate securely within the app, and integrated constraints to prevent blocked users from interacting. Furthermore, I developed the seller rating system (1 to 5 stars), ensuring robust backend validation to prevent self-rating or duplicate reviews.

For the administrative side, I developed the Admin Dashboard, which empowers administrators to moderate advertisements (approve/reject) and manage user accounts (block/unblock), including crucial logic to prevent admins from accidentally blocking themselves. I also refined the frontend navigation flow, implementing a guest mode with `permitAll` endpoints for public browsing and ensuring smooth redirects post-login.

---

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

### 1. Running the Backend (Server)
1. Navigate to the `backend` directory.
2. Ensure the Maven dependencies are successfully downloaded.
3. Run the `BackendApplication.java` main class.
4. The Spring Boot server will start on `http://localhost:8080`.
5. *Note: The SQLite database (`secondhand.db`) is already pre-populated with test data for your convenience.*

### 2. Running the Frontend (Client)
1. Open a new terminal/window and navigate to the `frontend` directory.
2. Ensure JavaFX and Jackson dependencies are properly loaded via Maven.
3. Run the `HelloApplication.java` main class.
4. The JavaFX application window will launch, ready to communicate with the backend.

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
