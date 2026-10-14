package io.nology.supportdesk.common;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public abstract class BaseE2ETest {
    @LocalServerPort
    private int port;

//    private final JwtService jwtService;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
    }

//   @Autowired
//   public BaseE2ETest(JwtService jwtService){
//       this.jwtService = jwtService;
//   }
//
//    protected RequestBuilder spec(){
//        return new RequestBuilder(port, "http://localhost", jwtService);
//    }
}

