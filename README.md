Skill Swap Hub 🔄

Skill Swap Hub is a web-based platform that allows users to share their skills and learn new skills from other users. Instead of traditional paid courses, users can connect with each other and exchange knowledge through a simple skill-sharing system.

🚀 Features
👤 User Registration and Login
🔐 User Authentication
🧑‍💻 Add and manage skills
🔎 Explore skills offered by other users
📚 Select skills you want to learn
🤝 Send skill exchange requests
✅ Accept or reject exchange requests
📊 User dashboard
💾 Store user and skill information in MySQL
🌐 REST APIs using Spring Boot
📱 Responsive and user-friendly interface
🛠️ Technologies Used
Frontend
HTML5
CSS3
JavaScript
Backend
Java
Spring Boot
Spring Data JPA
Hibernate
REST API
Database
MySQL
Deployment
Vercel – Frontend
Render – Backend
Aiven – MySQL Database
Development Tools
IntelliJ IDEA
Visual Studio Code
Git
GitHub
Maven
⚙️ How the Project Works
1. Registration

A new user creates an account by providing their basic details.

User
 ↓
Registration
 ↓
Spring Boot API
 ↓
MySQL Database
2. Login

The user logs into the platform using their credentials.

User
 ↓
Login
 ↓
Authentication API
 ↓
Database verification
 ↓
Dashboard
3. Add Skills

Users can add skills they are able to teach.

For example:

Java
HTML
CSS
Python
JavaScript
4. Explore Skills

Users can search and explore skills offered by other users.

5. Skill Exchange Request

A user can send a request to another user to exchange skills.

Example:

User A
Teaches: Java

User B
Teaches: Python

User A → Request → User B

The request can be:

PENDING
ACCEPTED
REJECTED
🔌 Backend API

The backend provides REST APIs for the frontend.

Example API structure:

/api/auth
/api/users
/api/skills
/api/exchange-requests

The exact endpoints depend on the controllers implemented in the backend.

🗄️ Database

The application uses MySQL to store application data.

Main entities include:

Users
Skills
Exchange Requests

Relationships between users and skills allow the application to determine:

Skills a user can teach
Skills a user wants to learn
Exchange requests between users
🐳 Docker

The Spring Boot backend can be built using Docker.

Example Dockerfile:

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["sh", "-c", "java -jar target/*.jar"]
🔧 Local Setup
Prerequisites

Install:

Java 17
Maven
MySQL
Git
IntelliJ IDEA
VS Code
Clone the Repository
git clone https://github.com/archita5793g/Skill-Swap-Hub.git

Go into the backend directory:

cd Skill-Swap-Hub/skillswaphub
Configure Database

Create a MySQL database:

CREATE DATABASE skillswapdb;

For local development, configure the database connection in application.properties.

Example:

spring.datasource.url=jdbc:mysql://localhost:3306/skillswapdb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
Run Backend

Using Maven:

./mvnw spring-boot:run

On Windows:

mvnw.cmd spring-boot:run

The backend will run on:

http://localhost:8080
🔐 Security
Database passwords should not be committed to GitHub.
Use environment variables for production credentials.
Use HTTPS for deployed applications.
Authentication and authorization should be handled by the backend.
🎯 Future Enhancements

Possible future improvements include:

Real-time chat between users
User ratings and reviews
Skill recommendations
Advanced skill search and filtering
Notifications
Profile customization
Skill verification
AI-based skill matching
Email notifications
Improved authentication with JWT
👩‍💻 Author

Archita Garg

GitHub:
https://github.com/archita5793g

📄 License

This project is developed for educational and project purposes.
