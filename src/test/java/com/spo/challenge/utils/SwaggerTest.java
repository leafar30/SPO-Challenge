package com.spo.challenge.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testSwagger() {

        //When/Then
        webTestClient.get().uri("/swagger-ui.html").exchange().expectStatus().isOk();

    }

}
