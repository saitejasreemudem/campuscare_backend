# CampusCare deployment

## Render backend and PostgreSQL

1. Push this backend repository to a Git provider connected to Render.
2. In Render, create a Blueprint Instance and select the backend repository. Render will read `render.yaml`, build the Spring Boot API from `Dockerfile`, and create the PostgreSQL database.
3. Wait for the `campuscare-api` web service to finish deploying. Its public URL will look like `https://campuscare-api.onrender.com`.

The Blueprint supplies database host, port, database name, username, and password as environment variables. No database secret is committed to this repository. Render's free PostgreSQL plan is temporary; choose a paid database plan if this needs to keep production data beyond the free plan's retention period.

## Vercel frontend

In the Vercel project for `Frontend/myapp`, add this Production environment variable:

```text
VITE_API_BASE_URL=https://<your-render-service>.onrender.com/api
```

Replace the host with the actual Render service URL, then redeploy the Vercel frontend. The backend CORS allowlist is preconfigured for `https://campuscare-frontend-three.vercel.app`.

For local development, the frontend defaults to `http://localhost:8080/api`; the backend defaults to PostgreSQL at `localhost:5432` with database `campuscare` and user `postgres`. Override the datasource settings with environment variables when using different local credentials.