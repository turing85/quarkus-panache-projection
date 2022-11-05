package de.turing85.panache.projection;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerTestResource implements QuarkusTestResourceLifecycleManager {
  private final PostgreSQLContainer<?> postgresContainer;
  private final String username;
  private final String password;
  private final String databaseName;

  @SuppressWarnings("unused")
  public PostgresContainerTestResource() {
    this(
        "postgres@sha256:5ea2da1f0275e38a6f39735b48a3688aae6ddfa2ad15971722d6fc0f2152918b",
        "app",
        "app",
        "app");
  }

  public PostgresContainerTestResource(String imageName, String username,
                                       String password, String databaseName) {
    this.username = username;
    this.password = password;
    this.databaseName = databaseName;
    this.postgresContainer = new PostgreSQLContainer<>(imageName)
        .withUsername(username)
        .withPassword(password)
        .withDatabaseName(databaseName);
  }

  @Override
  public Map<String, String> start() {
    postgresContainer.start();
    return Map.of(
        "quarkus.datasource.jdbc.url", postgresContainer.getJdbcUrl(),
        "quarkus.datasource.username", username,
        "quarkus.datasource.password", password,
        "quarkus.datasource.db-name", databaseName);
  }

  @Override
  public void stop() {
    postgresContainer.stop();
    postgresContainer.close();
  }
}
