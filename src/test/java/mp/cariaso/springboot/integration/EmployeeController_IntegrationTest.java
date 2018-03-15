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

        Employee employee = getEmployee(id);

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
                .log().everything();

        Long id = response.extract().body().as(Long.class);
        assertNotNull(id);
        logger.info("ID: " + id);

    }

    @Test
    public void updateEmployee()  {

        Long id = 2L;
        Employee existingEmployee = getEmployee(id);

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

        Employee updatedEmployee = getEmployee(id);

        assertNotEquals(existingEmployee.getDepartment(), updatedEmployee.getDepartment() );
        assertEquals(existingEmployee.getName(), updatedEmployee.getName());
        assertEquals(existingEmployee.getTitle(), updatedEmployee.getTitle());
    }

    /*

    public Employee deleteEmployee(long employeeId, String accessToken)  {

        ValidatableResponse response =

            given().
                keystore("D:\\apache-tomcat-8.0.30\\conf\\tomcat.jks", "tomcat").
                pathParam("id", employeeId).
                header("Authorization", "Bearer " + accessToken.trim()).
                header("Accept","application/vnd.shipserv.hr+json").
                baseUri("https://localhost"). //for SSL request
                port(8443). //for SSL request
                log().all().
                when().
                delete(SERVICE_CONTEXT_URI + "/employee/delete/{id}").
                then().
                log().all();

        Employee deletedEmployee = response.extract().body().as(Employee.class);

        return deletedEmployee;
    }
   */

    private Employee getEmployee(Long id) {


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

        Employee employee = response.extract().body().as(Employee.class);

        //assertThat(employee.getId(), equalTo(id));

        return employee;
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
