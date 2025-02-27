# Product Order System

*A simple product order system that follows a microservices approach, made for studying purposes.*

⚠️ **This project is a work in progress.** Some features are still under development.
## Tech Stack
- Java 17
- Spring Boot
- Kafka
- Observability with Grafana (Prometheus, Loki, Tempo)
#### Tests
- JUnit5
- Mockito
#### Database
- MongoDB
- PostgreSQL
## Services
- Product Service *(Spring Boot, MongoDB)*
- Order Service *(Spring Boot, PostgreSQL)*
- Notification Service *(Listens to Kafka messsages)*
- Inventory Service *(Spring, Boot, PostgreSQL)*
- Discovery Service *(Eureka Server)*
- API Gateway *(Spring Cloud Gateway)*

## How to run
Ensure you have the following installed:
- Java 17
- Docker & Docker Compose

1. Start services with docker:
	```bash
	docker compose up -d
	```
2. Go inside each service folder and run:
	```bash
	mvn spring-boot:run
	```
