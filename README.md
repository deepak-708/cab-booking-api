# 🚕 Ride-Hailing Backend API

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)

A robust, production-ready backend service simulating the core mechanics of a ride-hailing platform (like Uber or Ola). It handles user authentication, driver-rider matching, trip lifecycle management, and dynamic fare calculation.

**🚀 Live API Documentation (Swagger UI):** [Click here to view the live API](https://cab-booking-api-1.onrender.com/swagger-ui/index.html)

---

## 🏗️ System Architecture & Tech Stack

* **Framework:** Java / Spring Boot 3.x
* **Database:** PostgreSQL (Migrated from MySQL for cloud compatibility)
* **ORM:** Spring Data JPA / Hibernate
* **Security:** Spring Security (Role-based access control)
* **Containerization:** Docker & Docker Compose
* **Cloud Deployment:** Render (Web Service & Managed PostgreSQL)
* **Monitoring:** Spring Boot Actuator

---

## ✨ Key Features

* **Entity Management:** Bi-directional relational mapping between Riders, Drivers, and Trips with automated cascade deletions to maintain data integrity.
* **Secure API Endpoints:** Public routes for registration (`/api/riders/register`) and secured routes requiring authentication for ride operations.
* **Business Logic:** Custom algorithms for driver allocation and automated trip fare estimation based on distance and time.
* **DevOps & Containerization:** Fully containerized setup allowing for 1-click local deployment using `docker-compose`, completely isolating the database and application runtimes.
* **Observability:** Exposed Actuator endpoints (`/actuator/health`, `/actuator/metrics`) to track live JVM memory usage, database connection pools, and API response times.

---

## 🛠️ Local Development Setup

To run this project locally, you only need **Docker** installed on your machine. You do not need to install Java or PostgreSQL manually.

### 1. Clone the repository
```bash
git clone https://github.com/deepak-708/cab-booking-api.git
cd cab-booking-api