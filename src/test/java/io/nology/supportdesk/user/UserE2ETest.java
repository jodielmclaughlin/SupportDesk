package io.nology.supportdesk.user;

import io.nology.supportdesk.common.BaseE2ETest;
import io.nology.supportdesk.user.dtos.CreateUserDTO;
import io.nology.supportdesk.user.entity.UserRole;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class UserE2ETest extends BaseE2ETest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void createUser_withValidData_returnsCreatedUserWithoutPassword() {
        CreateUserDTO request = new CreateUserDTO();
        request.setName("Fake");
        request.setEmail("fakeuser@example.com");
        request.setPasswordHash("plainPassword123");
        request.setRole(UserRole.REQUESTER);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("Fake"))
                .body("email", equalTo("fakeuser@example.com"))
                .body("role", equalTo("REQUESTER"))
                .body("$", not(hasKey("passwordHash")));
    }

    @Test
    public void createUser_withInvalidEmail_returns400(){
        CreateUserDTO request = new CreateUserDTO();
        request.setName("Fake");
        request.setEmail("fakeuseremail");
        request.setPasswordHash("plainPassword123");
        request.setRole(UserRole.REQUESTER);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(400);
    }
    @Test
    public void createUser_withMissingField_returns400(){
        CreateUserDTO request = new CreateUserDTO();
        request.setName(" ");
        request.setEmail("fakeuser@example.com");
        request.setPasswordHash("plainPassword123");
        request.setRole(UserRole.REQUESTER);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(400);
    }

    @Test
    public void createUser_withDuplicateEmail_returns409(){
        CreateUserDTO originalUser = new CreateUserDTO();
        originalUser.setName("Fake");
        originalUser.setEmail("fakeuser@example.com");
        originalUser.setPasswordHash("plainPassword123");
        originalUser.setRole(UserRole.REQUESTER);

        userService.createUser(originalUser);

        CreateUserDTO request = new CreateUserDTO();
        request.setName("Fake");
        request.setEmail("fakeuser@example.com");
        request.setPasswordHash("plainPassword123");
        request.setRole(UserRole.REQUESTER);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", containsString("User with email " + request.getEmail() + " already exists."))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));
    }

}

