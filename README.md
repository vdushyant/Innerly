# Innerly

Innerly is a personal journal application with a Spring Boot backend and a separate web frontend.

## Features

- Create an account and sign in with JWT authentication.
- Create, search, read, edit, and delete journal entries.
- Keep each user's entries scoped to their account.
- Use a responsive web interface on desktop or mobile.

## Technologies used

- Java 17, Spring Boot, Spring Security, and Maven
- MongoDB for users and journal entries
- JWT for API authentication
- Vite, JavaScript, and CSS for the frontend
- Redis for weather-response caching

## Prerequisites

- Java 17
- Docker Desktop with MongoDB available as a replica set
- Node.js and npm

## Local setup

Start MongoDB locally as a replica set, then run the backend and frontend in separate terminals.

From the backend project directory, start the API:

```powershell
.\mvnw.cmd spring-boot:run
```

From the frontend project directory, install dependencies and start the web app:

```powershell
npm install
npm run dev
```

Open the local URL printed by Vite. The frontend connects to the backend through its development proxy.

## API requests

Import the [Innerly Postman collection](docs/spring%20boot%20journal.postman_collection.json) to explore the API. Set its `user_name` and `password` variables, then run **Sign up** and **Log in** before using authenticated requests.

## Contribution

Contributions are welcome! Feel free to open issues and submit pull requests.

## License

This project is licensed under the MIT License.
