# Book & Checkout Services

This repository contains two Quarkus-based microservices:

- **Book Service** (on port 8080)
- **Checkout Service** (on port 8081)

A `compose.yml` file is provided to spin up the following containers:

1. **Redpanda (Kafka)** – for messaging.
2. **MongoDB** – for the Book Service’s database.
3. **Book Service** – Quarkus application.
4. **Checkout Service** – Quarkus application.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) installed.
- [Docker Compose](https://docs.docker.com/compose/) installed (or Docker CLI with Compose v2).

## Getting Started

1. **Clone this repository** (if not already):
   ```bash
   git clone https://github.com/wingi11/quark-messaging.git
   cd <your-repo>
   ```

2. **Start the services**:
   ```bash
   docker compose up
   ```
   This command will:
   - Pull the **book-service** and **checkout-service** images from github container registry.
   - Launch Redpanda, MongoDB, Book Service, and Checkout Service containers.

3. **Verify startup**:
   - Once containers are up, you should see logs indicating Quarkus has started on ports 8080 (book-service) and 8081 (checkout-service).

4. **Access the services**:
   - **Book Service** Swagger UI: [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)  
   - **Checkout Service** Swagger UI: [http://localhost:8081/q/swagger-ui](http://localhost:8081/q/swagger-ui)

   You can use the Swagger UI pages to test the endpoints or review available operations.

5. **Stop the services**:
   - Press `Ctrl + C` in the terminal running `docker compose up`.
   - Alternatively, run:
     ```bash
     docker compose down
     ```
     from another shell, which stops and removes all the containers.

## Additional Notes

- **Kafka (Redpanda)** is mapped to port `9092` on your host machine.
- **MongoDB** is mapped to port `27017` on your host machine (database name: `books`).
- **Book Service** listens on `8080`.
- **Checkout Service** listens on `8081`.

If you need to customize ports, environment variables, or database configurations, edit the `compose.yml` and/or the Quarkus application properties within each service before starting the containers.

## Grafana Dashboard and Alert Rules

- **Grafana** is reachable under [http://localhost:3000](http://localhost:3000).

### Importing the Stats Dashboard

To import the `stats-dashboard.json` file in Grafana, follow these steps:

1. Open your web browser and navigate to [http://localhost:3000](http://localhost:3000).
2. Log in to Grafana (default credentials are typically **admin** for both username and password).
3. In the left sidebar, click on the **+** icon (Create) and select **Import**.
4. Click on **Upload .json File** and select the `stats-dashboard.json` file from your local machine.
5. Click **Load** to preview the dashboard, then click **Import** to add it to your Grafana instance.

### Note on Alert Rules

- The `alert-rules.json` file contains the exported alert rules. Currently, it is not possible to import these alerts via the Grafana UI. This is provided as informational only.
