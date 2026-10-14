package io.nology.supportdesk.common;

import io.nology.supportdesk.user.entity.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestBuilder {
    private static final Logger log = LogManager.getLogger(RequestBuilder.class);
    private final RequestSpecBuilder requestSpecBuilder;
    //private final JwtService jwtService;
//, JwtService jwtService
    public RequestBuilder(int port, String baseUri) {
        this.requestSpecBuilder = new RequestSpecBuilder().setPort(port).setBaseUri(baseUri)
                .setContentType(ContentType.JSON);
        //this.jwtService = jwtService;
    }

//    public RequestBuilder withJwt(User user){
//        //String token = this.jwtService.generateAccessToken(user);
//        //log.info("Created JWT: {}", token);
//        //requestSpecBuilder.addHeader("Authorization", "Bearer " + token);
//        //return this;
//    }

    public RequestSpecification build(){
        return RestAssured.given(this.requestSpecBuilder.build());
    }
}