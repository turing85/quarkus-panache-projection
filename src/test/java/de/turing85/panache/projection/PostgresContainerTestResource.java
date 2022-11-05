package de.turing85.panache.projection;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerTestResource implements QuarkusTestResourceLifecycleManager {
  private final PostgreSQLContainer<?> container;

  @SuppressWarnings("unused")
  public PostgresContainerTestResource() {
    this(
        "postgres@sha256:5ea2da1f0275e38a6f39735b48a3688aae6ddfa2ad15971722d6fc0f2152918b",
        "app",
        "app",
        "app");
  }

  private PostgresContainerTestResource(
      String imageName,
      String username,
      String password,
      String databaseName) {
    this.container = new PostgreSQLContainer<>(imageName)
        .withUsername(username)
        .withPassword(password)
        .withDatabaseName(databaseName);
  }

  @Override
  public Map<String, String> start() {
    container.start();
    return Map.of(
        "quarkus.datasource.jdbc.url", container.getJdbcUrl(),
        "quarkus.datasource.username", container.getUsername(),
        "quarkus.datasource.password", container.getPassword(),
        "quarkus.datasource.db-name", container.getDatabaseName());
  }

  @Override
  public void stop() {
    container.stop();
    container.close();
  }
}