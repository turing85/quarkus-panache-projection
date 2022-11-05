package de.turing85.panache.projection;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;

import de.turing85.panache.projection.entity.Data;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Comparator;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(PostgresContainerTestResource.class)
@TestHTTPEndpoint(DataResource.class)
class DataResourceTest {
  public static final List<Data> TEST_DATA = List.of(
      new Data(0L, "foo"),
      new Data(1L, "bar"),
      new Data(2L, "baz"));

  @Test
  void createAndDelete() {
    Data newData = postNewData();
    verifyDataIsPresent(newData);
    deleteById(newData.getId());
    verifyDataIsAbsent(newData);
  }

  private static Data postNewData() {
    // @formatter:off
    // GIVEN
    String expectedName = "expectedName";
    Data actual = given()
        .contentType(ContentType.APPLICATION_JSON.getMimeType())
        .body(expectedName)

    // WHEN
        .when()
            .post()

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().as(Data.class);
    assertThat(actual.getId()).isNotNull();
    assertThat(actual.getName()).isEqualTo(expectedName);
    return actual;
    // @formatter:on
  }

  private static void verifyDataIsPresent(Data actual) {
    // @formatter:off
    // GIVEN
    List<Data> all = given()
    // WHEN
        .when()
            .get()

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
                .body().jsonPath().getList(".", Data.class);
    assertThat(all).contains(actual);
    // @formatter:on
  }

  private static void deleteById(long id) {
    // @formatter:off
    // GIVEN
    given()
        .pathParam("id", id)

    // WHEN
        .when()
            .delete("{id}")

    // THEN
        .then()
            .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    // @formatter:on
  }

  private static void verifyDataIsAbsent(Data actual) {
    // @formatter:off
    // GIVEN
    List<Data> all = given()

    // WHEN
        .when()
            .get()

        // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().jsonPath().getList(".", Data.class);
    assertThat(all).doesNotContain(actual);
    // @formatter:on
  }

  @Test
  void testGetAll() {
    // @formatter:off
    // GIVEN
    List<Data> expected = TEST_DATA.stream().sorted(Comparator.comparing(Data::getId)).toList();
    List<Data> actual = given()

    // WHEN
        .when()
            .get()

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().jsonPath().getList(".", Data.class);
    assertThat(actual).isEqualTo(expected);
    // @formatter:on
  }

  @Test
  void testGetAllIds() {
    // @formatter:off
    // GIVEN
    List<Long> expected = TEST_DATA.stream().map(Data::getId).sorted().toList();
    List<Long> actual = given()

    // WHEN
        .when()
            .get("ids")

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().jsonPath().getList(".", Long.class);
    assertThat(actual).isEqualTo(expected);
    // @formatter:on
  }
}