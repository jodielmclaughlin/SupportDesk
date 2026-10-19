package io.nology.supportdesk.ticket;

import io.nology.supportdesk.common.BaseE2ETest;
import io.nology.supportdesk.config.factory.ticket.TicketFactory;
import io.nology.supportdesk.config.factory.ticket.TicketFactoryOptions;
import io.nology.supportdesk.config.factory.user.UserFactory;

import io.nology.supportdesk.ticket.entity.Ticket;
import io.nology.supportdesk.user.entity.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;



public class TicketE2ETest extends BaseE2ETest {

    private TicketFactory ticketFactory;
    private UserFactory userFactory;

    @Autowired
    public TicketE2ETest(TicketFactory ticketFactory, UserFactory userFactory){
        this.ticketFactory = ticketFactory;
        this.userFactory = userFactory;
    }

    @Test
    public void getAllTickets_returnsOKAndListOfTickets(){
        User fakeCreator = this.userFactory.create();
        User fakeAssigned = this.userFactory.create();
        TicketFactoryOptions options = TicketFactoryOptions.builder().assignedTo(fakeAssigned).createdBy(fakeCreator).build();
        this.ticketFactory.create(options, 10);
        given()
                .when()
                .get("/tickets")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(10))
                .body(matchesJsonSchemaInClasspath("schema/ticket-list-schema.json"));
    }

    @Test
    public void getAllTickets_whenNoTickets_returnsEmptyArray(){
        given()
                .when()
                .get("/tickets")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(0));

    }

    @Test
    public void getById_validId_returnsTicket(){
        User fakeCreator = this.userFactory.create();
        User fakeAssigned = this.userFactory.create();
        TicketFactoryOptions options = TicketFactoryOptions.builder()
                .title("Test Ticket")
                .description("This is a test ticket")
                .assignedTo(fakeAssigned)
                .createdBy(fakeCreator)
                .build();

        Ticket fakeTicket = this.ticketFactory.create(options);
        Long id = fakeTicket.getId();


        given()
                .when()
                .get("/tickets/" + id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo("Test Ticket"))
                .body("description", equalTo("This is a test ticket"))
                .body("status", equalTo("OPEN"))
                .body("createdBy", equalTo(fakeCreator.getName()))
                .body(matchesJsonSchemaInClasspath("schema/ticket-schema.json"));
    }

    @Test
    public void getById_nonExistentId_returns404(){
        given()
                .when()
                .get("/tickets/" + 1L)
                .then().statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(404))
                .body("error", equalTo("Not Found"))
                .body("message", equalTo("Ticket not found with ID: 1"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));

    }

    @Test
    public void getById_invalidDataTypeId_returnsBadRequest(){
        given().when().get("/tickets/" + "banana")
                .then().log().all().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("Failed to convert value"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));

    }

    @Test
    public void createTicket_withValidData_returnsCreatedTicket(){
        User fakeCreator = this.userFactory.create();
        User fakeAssigned = this.userFactory.create();

        HashMap<String, String> data = new HashMap<>();
        data.put("title", "Test Ticket");
        data.put("description", "This is a test ticket");
        data.put("createdBy", fakeCreator.getId().toString());
        data.put("assignedTo", fakeAssigned.getId().toString());
        data.put("status", "OPEN");
        data.put("category", "HARDWARE");
        data.put("priority", "MEDIUM");

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/tickets")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("title", equalTo("Test Ticket"))
                .body("description", equalTo("This is a test ticket"))
                .body("status", equalTo("OPEN"))
                .body("createdBy", equalTo(fakeCreator.getName()))
                .body(matchesJsonSchemaInClasspath("schema/ticket-schema.json"));
    }

    @Test
    public void createTicket_withMissingField_returns400(){
        User fakeCreator = this.userFactory.create();
        User fakeAssigned = this.userFactory.create();

        HashMap<String, String> data = new HashMap<>();
        data.put("title", "Test Ticket");
        data.put("description", " ");
        data.put("createdBy", fakeCreator.getId().toString());
        data.put("assignedTo", fakeAssigned.getId().toString());
        data.put("status", "OPEN");
        data.put("category", "HARDWARE");
        data.put("priority", "MEDIUM");

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/tickets")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    public void updateTicket_whenUpdatingExistingTicket_returnsOKWithUpdatedDetails() {
        User fakeCreator = this.userFactory.create();
        User fakeAssigned = this.userFactory.create();

        TicketFactoryOptions options = TicketFactoryOptions.builder().assignedTo(fakeAssigned).createdBy(fakeCreator).build();
        Ticket fakeTicket = this.ticketFactory.create(options);
        HashMap<String, String> data = new HashMap<>();
        data.put("title", "Updated Test");
        given().contentType(ContentType.JSON).body(data).when().log().all()
                .patch("/tickets/" + fakeTicket.getId())
                // assert
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo("Updated Test"))
                .body(matchesJsonSchemaInClasspath("schema/ticket-schema.json"));
    }


    @Test
    public void updateTicket_whenNoTicketExists_returns404NotFound() {
        HashMap<String, String> data = new HashMap<>();
        data.put("firstName", "updated");
        given().contentType(ContentType.JSON).body(data).when().log().all()
                .patch("/tickets/2")
                // assert
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("Ticket not found with ID: 2"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));
    }

    @Test
    public void deleteTicket_whenNoTicketExists_returns404NotFound() {
        given()
                .when()
                .delete("tickets/2")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("Ticket not found with ID: 2"))
                .body(matchesJsonSchemaInClasspath("schema/api-error-response-schema.json"));
    }

    @Test
    public void deleteTicket_whenTicketExists_ReturnsNoContent() {
        User fakeCreator = this.userFactory.create();
        User fakeAssigned = this.userFactory.create();

        TicketFactoryOptions options = TicketFactoryOptions.builder().assignedTo(fakeAssigned).createdBy(fakeCreator).build();
        Ticket fakeTicket = this.ticketFactory.create(options);

        given().when().log().all()
                .delete("/tickets/" + fakeTicket.getId())
                // assert
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }



}

