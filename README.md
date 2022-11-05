# quarkus-panache-projection Project

A small project to show how to use projections in panache.

To build the project, run
```bash
./mvnw verify
```

This will execute the integration tests (which will start a database container through testcontainers) and record the test coverage with JaCoCo.

To run the application, run

```bash
./mvnw quarkus:dev
```

This will start a postgres container through devservices.

The application provides a swagger-ui under [`http://localhost:8080/q/swagger-ui`](http://locahost:8080/q/swagger-ui), so we can play around with it.