package com.spo.challenge.utils;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActuatorTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testHealth() throws JSONException {
        //given
        final String expected = "{\"status\":\"UP\"}";

        //When/Then
        final String result = webTestClient.get()
                                           .uri("/actuator/health")
                                           .exchange()
                                           .expectStatus()
                                           .isOk()
                                           .expectBody(String.class)
                                           .returnResult()
                                           .getResponseBody();

        JSONAssert.assertEquals(expected, result, true);

    }

}
