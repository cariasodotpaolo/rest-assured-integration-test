package mp.cariaso.springboot.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ValidatableResponse;
import java.util.Arrays;
import java.util.List;
import mp.cariaso.springboot.api.request.EmployeeRequest;
import mp.cariaso.springboot.model.Employee;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EmployeeController_IntegrationTest extends IntegrationTestBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void getEmployees() {


        ValidatableResponse response =

            given()
                .header("Authorization", RandomStringUtils.randomAlphabetic(12))
                .header("Accept","application/json")
                .log().all()
            .when()
                .get( getContextUrl() + "/employees/list")
            .then()
                .log().all();

        List<Employee> employees = Arrays.asList(response.extract().body().as(Employee[].class));

        assertThat(employees.size(), greaterThan(0));
    }


    @Test
    public void getEmployee()  {

        Long id = 2L;

        ValidatableResponse response = getEmployeeResponse(id);

        assertThat(response.extract().statusCode(), equalTo(200));

        Employee employee = response.extract().body().as(Employee.class);

        assertThat(employee.getId(), equalTo(id));
    }

    @Test
    public void addEmployee()  {


        ValidatableResponse response =

            given()
                .header("Authorization", RandomStringUtils.randomAlphabetic(12))
                .header("Accept","application/json")
                .contentType("application/json")
                .body(givenNewEmployeeJson())
                .baseUri(getBaseUri())
                .port(getPort())
                .log().all()
            .when()
                .post("/employees/add")
            .then()
                .statusCode(200)
                .log().all();

        Long id = response.extract().body().as(Long.class);
        assertNotNull(id);
        logger.info("ID: " + id);

    }

    @Test
    public void updateEmployee()  {

        Long id = 2L;

        ValidatableResponse response = getEmployeeResponse(id);
        assertThat(response.extract().statusCode(), equalTo(200));
        Employee existingEmployee = response.extract().body().as(Employee.class);

            given()
                .header("Authorization", RandomStringUtils.randomAlphabetic(12))
                .header("Accept","application/json")
                .contentType("application/json")
                .pathParam("id", id)
                .body(givenUpdateEmployeeJson())
                .baseUri(getBaseUri())
                .port(getPort())
                .log().all()
            .when()
                .put("/employees/update/{id}")
            .then()
                .log().all()
                .statusCode(200);

        ValidatableResponse updatedResponse = getEmployeeResponse(id);
        assertThat(updatedResponse.extract().statusCode(), equalTo(200));
        Employee updatedEmployee = updatedResponse.extract().body().as(Employee.class);

        assertNotEquals ( existingEmployee.getDepartment(), updatedEmployee.getDepartment() );
        assertEquals ( existingEmployee.getName(), updatedEmployee.getName() );
        assertEquals ( existingEmployee.getTitle(), updatedEmployee.getTitle() );
    }

    @Test
    public void deleteEmployee()  {

        Long id = 4L;

            given().
                pathParam("id", id).
                header("Authorization", RandomStringUtils.randomAlphabetic(12)).
                baseUri(getBaseUri()).
                port(getPort()).
                log().all().
                when().
                delete("/employees/delete/{id}").
                then().
                log().all();

        ValidatableResponse response = getEmployeeResponse(id);
        assertThat(response.extract().statusCode(), equalTo(404));
    }


    private ValidatableResponse getEmployeeResponse(Long id) {


        ValidatableResponse response =

            given()
                .pathParam("id", id)
                .header("Authorization", RandomStringUtils.randomAlphabetic(12))
                .header("Accept","application/json")
                .baseUri(getBaseUri())
                .port(getPort())
                .log().all()
            .when()
                .get("/employees/{id}")
            .then()
                .log().all();

        return response;
    }

    private String givenNewEmployeeJson() {

        String differentiator = RandomStringUtils.randomAlphabetic(6);

        EmployeeRequest request = new EmployeeRequest(String.format("TEST_NAME-%s", differentiator),
            String.format("TEST_DEPT-%s", differentiator),
            String.format("TEST_TITLE-%s", differentiator));

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = jsonMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing object.", e);
        }

        return jsonString;
    }

    private String givenUpdateEmployeeJson() {

        String differentiator = RandomStringUtils.randomAlphabetic(6);

        EmployeeRequest request = new EmployeeRequest(null,
            String.format("TEST_UPDATED_DEPT-%s", differentiator),null);

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = jsonMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing object.", e);
        }

        return jsonString;
    }
}
