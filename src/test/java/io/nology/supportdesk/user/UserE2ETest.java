package io.nology.supportdesk.user;

import io.nology.supportdesk.common.BaseE2ETest;
import io.nology.supportdesk.user.dtos.CreateUserDTO;
import io.nology.supportdesk.user.dtos.UpdateUserDTO;
import io.nology.supportdesk.user.entity.User;
import io.nology.supportdesk.user.entity.UserRole;
import io.restassured.http.ContentType;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class UserE2ETest extends BaseE2ETest {

    @Autowired
    private UserService userService;

    @Test
    public void getById_validId_returnsUserAndDoesntExposePasswordHash(){
        CreateUserDTO request = new CreateUserDTO();
        request.setName("Fake");
        request.setEmail("fakeuser@example.com");
        request.setPasswordHash("plainPassword123");
        request.setRole(UserRole.REQUESTER);
        User fake = userService.createUser(request);
        Long id = fake.getId();

        given()
                .when()
                .get("/users/" + id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Fake"))
                .body("email", equalTo("fakeuser@example.com"))
                .body("role", equalTo("REQUESTER"))
                .body("$", not(hasKey("passwordHash")));

    }

    @Test
    public void getById_nonExistentId_returns404(){

        given()
                .when()
                .get("/users/" + 1L)
                .then().statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(404))
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("User not found with ID: 1"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));
    }

    @Test
    public void getById_invalidDataTypeId_returnsBadRequest(){
        given().when().get("/users/" + "banana")
                // assert
                .then().log().all().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("Failed to convert value"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));
    }

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
                .body("$", not(hasKey("passwordHash")))
                .body(matchesJsonSchemaInClasspath("schema/user-schema.json"));
    }

    @Test
    public void createUser_withInvalidEmail_returns400(){
        CreateUserDTO request = new CreateUserDTO();
        request.setName("Fake");
        request.setEmail("fakeuser.email");
        request.setPasswordHash("plainPassword123");
        request.setRole(UserRole.REQUESTER);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
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
                .statusCode(HttpStatus.BAD_REQUEST.value());
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

    @Test
    public void updateUser_whenUpdatingExistingUser_returnsOKWithUpdatedDetails(){
        CreateUserDTO request = new CreateUserDTO();
        request.setName("Fake");
        request.setEmail("fakeuser@example.com");
        request.setPasswordHash("plainPassword123");
        request.setRole(UserRole.REQUESTER);
        User fake = this.userService.createUser(request);

        UpdateUserDTO updatedUser = new UpdateUserDTO();
        updatedUser.setName("Updated");
        updatedUser.setRole(UserRole.ADMIN);

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when()
                .log()
                .all()
                .patch("/users/" + fake.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Updated"))
                .body("role", equalTo("ADMIN"))
                .body(matchesJsonSchemaInClasspath("schema/user-schema.json"));
    }

    @Test
    public void updateUser_whenNoExistingUser_returns404(){
        UpdateUserDTO updatedUser = new UpdateUserDTO();
        updatedUser.setName("Updated");
        updatedUser.setRole(UserRole.ADMIN);

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when()
                .patch("/users/2")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(404))
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("User not found with ID: 2"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));
    }

    @Test
    public void updateUser_whenReturningUpdatedUser_doesNotExposePasswordHash(){
        CreateUserDTO request = new CreateUserDTO();
        request.setName("Fake");
        request.setEmail("fakeuser@example.com");
        request.setPasswordHash("plainPassword123");
        request.setRole(UserRole.REQUESTER);
        User fake = this.userService.createUser(request);

        UpdateUserDTO updatedUser = new UpdateUserDTO();
        updatedUser.setName("Updated");
        updatedUser.setRole(UserRole.ADMIN);

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when()
                .log()
                .all()
                .patch("/users/" + fake.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Updated"))
                .body("role", equalTo("ADMIN"))
                .body("$", not(hasKey("passwordHash")))
                .body(matchesJsonSchemaInClasspath("schema/user-schema.json"));

    }

    @Test
    public void deleteUser_userDoesNotExist_returnsNotFound(){
        given()
                .when()
                .delete("/users/1")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("User not found with ID: 1"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));

    }

    @Test
    public void deleteUser_userExists_returnsNoContent(){
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Fake");
        dto.setEmail("fakeuser@example.com");
        dto.setPasswordHash("plainPassword123");
        dto.setRole(UserRole.REQUESTER);
        User user = this.userService.createUser(dto);


        given()
                .when()
                .delete("/users/" + user.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deleteUser_invalidDataTypeId_returnsBadRequest(){
        given().when().delete("/users/" + "banana")
                // assert
                .then().log().all().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("Failed to convert value"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));
    }

}

