package de.turing85.panache.projection;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import de.turing85.panache.projection.entity.Data;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Comparator;
import java.util.List;
import jakarta.ws.rs.core.Response;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(DataResource.class)
@DisplayName("Data endpoints")
class DataResourceTest {
  private static final List<Data> TEST_DATA = List.of(
      new Data(0L, "foo"),
      new Data(1L, "bar"),
      new Data(2L, "baz"));

  @Test
  @DisplayName("post new data, then delete it")
  void createAndDelete() {
    Data newData = postNewData();
    verifyDataIsPresent(newData);
    deleteById(newData.getId());
    verifyDataIsAbsent(newData);
  }

  private static Data postNewData() {
    // GIVEN
    String expectedName = "expectedName";
    // @formatter:off
    Data actual = given()
        .contentType(ContentType.APPLICATION_JSON.getMimeType())
        .body(expectedName)

    // WHEN
        .when().post()

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().as(Data.class);
    // @formatter:on
    assertThat(actual.getId()).isNotNull();
    assertThat(actual.getName()).isEqualTo(expectedName);
    return actual;
  }

  private static void verifyDataIsPresent(Data actual) {
    // WHEN
    // @formatter:off
    List<Data> all = when().get()

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract()
                .body().jsonPath().getList(".", Data.class);
    // @formatter:on
    assertThat(all).contains(actual);
  }

  private static void deleteById(long id) {
    // GIVEN
    // @formatter:off
    given()
        .pathParam("id", id)

    // WHEN
        .when().delete("{id}")

    // THEN
        .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    // @formatter:on
  }

  private static void verifyDataIsAbsent(Data actual) {
    // WHEN
    // @formatter:off
    List<Data> all = when().get()

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().jsonPath().getList(".", Data.class);
    // @formatter:on
    assertThat(all).doesNotContain(actual);
  }

  @Test
  @DisplayName("get all data")
  void testGetAll() {
    // GIVEN
    List<Data> expected = TEST_DATA.stream().sorted(Comparator.comparing(Data::getId)).toList();

    // WHEN
    // @formatter:off
    List<Data> actual = when().get()

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().jsonPath().getList(".", Data.class);
    // @formatter:on
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  @DisplayName("get all ids")
  void testGetAllIds() {
    // GIVEN
    List<Long> expected = TEST_DATA.stream().map(Data::getId).sorted().toList();

    // WHEN
    // @formatter:off
    List<Long> actual = when().get("ids")

    // THEN
        .then()
            .statusCode(Response.Status.OK.getStatusCode())
            .extract().body().jsonPath().getList(".", Long.class);
    // @formatter:on
    assertThat(actual).isEqualTo(expected);
  }
}