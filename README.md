# Second Hand Market - Advanced Programming Final Project

A comprehensive client-server application built with Spring Boot and JavaFX for buying and selling second-hand items. This project implements a real-world architecture separating the Backend logic and RESTful communication from the Frontend user interface, with data persistently stored using SQLite.

## 🛠 Technologies Used
* **Backend:** Java, Spring Boot 4.1.0, Spring Data JPA, Spring Security, JWT (jjwt 0.12.6)
* **Frontend:** JavaFX 21, FXML, Jackson (JSON Parsing)
* **Database:** SQLite
* **Architecture:** Client-Server / RESTful API

---

## 👥 Team Members & Contributions

This project was collaboratively developed by our team of two. Below is the breakdown of our responsibilities and implementations:

### 1. Mohammad Mehdi Hajizadeh
**Role:** Full-Stack Developer (Advertisements, Search/Filtering, Core UI, & Navigation)
* **Advertisement Management:** Implemented the core logic for creating, viewing, and managing ads with distinct statuses (`PENDING`, `ACTIVE`, `SOLD`).
* **Image Upload System:** Developed a robust image upload feature capable of handling up to 3 images per ad using Base64 encoding for seamless transmission and storage.
* **Advanced Search & Filtering:** Created a hybrid in-memory/backend filtering system allowing users to search by keywords, filter by City and Category, and apply strict numeric price range validations.
* **Favorites System:** Implemented the backend logic and frontend UI for users to add/remove ads to their personal favorites list.
* **Frontend Navigation & UI:**
    * Designed the Main Dashboard (Grid layout) and Ad Details UI.
    * Ensured strict UI constraints (e.g., hiding Chat and Rate buttons if an ad is not in the `ACTIVE` status).

### 2. Amirhossein Hosseinpour
**Role:** Full-Stack Developer (Authentication, Chat System, Rating, & Admin Dashboard)
* **User & Authentication:** Built the registration and login flows, secured by JWT authentication and BCrypt password hashing.
* **Chat System:** Implemented the conversation engine allowing buyers and sellers to communicate securely, including real-time simulation and checks to prevent blocked users from interacting.
* **Rating System:** Created the backend constraints and frontend UI for buyers to rate sellers (1 to 5 stars), preventing self-rating and duplicate ratings.
* **Admin Panel:** Developed the dashboard for administrators to manage users, monitor block statuses, and safely block/unblock users (with logic to prevent self-blocking).
* **Navigation & Security Polish:**
    * Implemented guest mode browsing with `permitAll` endpoints for public data (Cities, Categories).
    * Refined redirect paths post-login to seamlessly route users to the main view.
    * Removed initial redundant chat screens, routing users directly to their active conversation lists.
    * Handled deep-link navigation between teammate components (e.g., moving rating buttons to the chat interface).

* **Frontend Navigation & UI:**
  * Designed the Main Dashboard (Grid layout) and Ad Details UI.
  * Enhanced UI constraints (e.g., hiding Chat and Rate buttons if an ad is not in the `ACTIVE` status).
  * Refined window navigation logic to gracefully handle popup modals.
---

## 🚀 Key Features

### Base & Complete Features
* **User Authentication:** Secure JWT-based Login and Registration. Guest users can browse but are prompted to log in for interactions.
* **Advertisement Lifecycle:** Ads go through a cycle (`PENDING` -> `ACTIVE` -> `SOLD`). Only `ACTIVE` ads are publicly visible.
* **Media Support:** Upload and view multiple images for advertisements.
* **Smart Search:** Filter items dynamically based on category, location, text, and price boundaries.
* **Direct Communication:** Built-in messaging system connecting buyers directly to ad owners.
* **Reputation System:** 1-5 star rating system for sellers after interactions.
* **Admin Controls:** Dedicated panel for user management and ad moderation.

---

## ⚙️ How to Run the Project

### Prerequisites
* Java Development Kit (JDK) 21 or higher.
* Maven installed.
* An IDE (IntelliJ IDEA recommended).

### 1. Running the Backend
1. Navigate to the `backend` directory.
2. The SQLite database (`secondhand.db`) will be automatically generated in the root folder upon the first run.
3. Run the `BackendApplication.java` main class.
4. The server will start on `http://localhost:8080`.

### 2. Running the Frontend
1. Navigate to the `frontend` directory.
2. Ensure JavaFX dependencies are properly loaded via Maven.
3. Run the `HelloApplication.java` main class.
4. The JavaFX application window will launch.

### 3. Test Accounts
* **Admin:** (Please create a user directly in the DB and set role to `ROLE_ADMIN` or use the default seeder if provided).
* **User:** You can register a new user normally through the application's GUI.