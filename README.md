📓 JournalApp — Secure Personal Journal API
<img width="825" height="631" alt="image" src="https://github.com/user-attachments/assets/b804aebf-cb0c-4ca7-97a8-3ae8659212cd" />

A high-quality, production-grade journaling application built with Spring Boot, Java, and MongoDB.
JournalApp is designed with a security-first mindset — featuring JWT authentication, role-based access, scheduled sentiment analysis, and email reporting.

✨ Key Features

✅ Secure Authentication — Full JWT implementation for user signup, login, and endpoint protection.
🗒️ Full CRUD API — Create, Read, Update, and Delete your journal entries.
🧑‍💻 Role-Based Access — USER and ADMIN role differentiation (/admin/** endpoints).
📘 Interactive API Docs — Live Swagger UI (SpringDoc / OpenAPI).
⏰ Scheduled Tasks — Weekly cron jobs for sentiment analysis + email summaries.
🌦️ Weather API Integration — Fetch live weather data from Weatherstack API.
📧 Email Notifications — Automatic email sending via GMail SMTP.
🧠 Sentiment Analysis — Uses external API + scheduled reports for insights.
🧪 Testing & Code Quality — 90%+ test coverage with JUnit 5, Mockito, and SonarQube.

🛠️ Tech Stack
Layer	Technology
Framework	Spring Boot 3.5.6
Language	Java 17
Security	Spring Security 6 (JWT)
Database	MongoDB Atlas (Spring Data MongoDB)
Testing	JUnit 5
Build Tool	Apache Maven
Utilities	Lombok, RestTemplate, GMail SMTP
Code Quality	SonarCloud / SonarQube
🔒 Security & Code Quality

🧩 Zero Hardcoded Secrets:
All sensitive values (DB URIs, API Keys, JWT Secrets) are injected via environment variables.

🔐 BCrypt Password Hashing:
All passwords are securely stored after hashing.

🧱 DTO Protection:
All controllers use DTOs to prevent data leaks and mass assignment vulnerabilities.

🧰 Static Analysis with SonarQube:
100% of reported bugs, vulnerabilities, and code smells have been fixed.

<img width="1920" height="907" alt="Screenshot (341)" src="https://github.com/user-attachments/assets/f75c8f0e-49b4-4797-ad58-4276b4ba3a88" />

🚀 Getting Started
Prerequisites

Before you start, make sure you have:

☕ Java 17 or higher
🧱 Apache Maven
🍃 MongoDB Atlas (free account)
📧 GMail App Password (16-digit)
🌦️ Weatherstack API Key
⚙️ Setup & Configuration
1️⃣ Clone the repository

git clone https://github.com/mihir021/journalApp.git
cd journalApp


2️⃣ Set environment variables

In your IDE or terminal:

# MongoDB Atlas connection
MONGO_URI=mongodb+srv://<username>:<password>@<cluster-url>/<dbname>?retryWrites=true&w=majority

# GMail Credentials
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-16-digit-app-password

# Weatherstack API Key
WEATHER_API_KEY=your-weatherstack-api-key

# JWT Secret Key
JWT_SECRET_KEY=your-own-long-random-secret-key-for-jwt


3️⃣ Build and run the project

mvn clean install
mvn spring-boot:run


The app will start at 👉 http://localhost:8080

📖 API Documentation (Swagger UI)

Once the app is running, visit:
👉 http://localhost:8080/swagger-ui.html

🪄 How to Test Secure Endpoints

Signup:
POST /public/signup → Create a new user.

Login:
POST /public/login → Receive your JWT token.

Authorize:
In Swagger, click Authorize → paste your token as:

Bearer <your-token>


Access:
Now you can call secured endpoints like /journal, /user, and /admin.

🧠 Scheduler & Email Insights

Every Sunday midnight (configurable via @Scheduled)

The app performs sentiment analysis on recent journals

Sends weekly summary emails to users 📬

🧪 Running Tests
mvn test
All tests use JUnit 5 .

Pull requests are welcome!
Feel free to fork this repo and enhance it further.

git checkout -b feature/my-feature
git commit -m "Added cool new feature"
git push origin feature/my-feature
