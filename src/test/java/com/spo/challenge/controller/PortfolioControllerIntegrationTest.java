package com.spo.challenge.controller;

import com.spo.challenge.dto.CleaningOrganizeDto;
import com.spo.challenge.dto.CleaningOrganizersDto;
import com.spo.challenge.dto.PortfolioInfoDto;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PortfolioControllerIntegrationTest {

    public static final String URL = "/organize/portfolios";
    @Autowired
    private WebTestClient webTestClient;

    /*
     *In: { “rooms”: [35, 21, 17, 28], “senior”: 10, “junior”: 6 }
     *Out: [ {senior: 3, junior: 1}, {senior: 1, junior: 2}, {senior: 2, junior: 0}, {senior: 1, junior: 3} ]
     */
    @Test
    public void shouldOrganizeCase1() {

        //Given
        final PortfolioInfoDto request = PortfolioInfoDto.builder().jrCapacity(6).srCapacity(10).structures(List.of(21, 35, 17, 28)).build();

        final CleaningOrganizersDto expected = CleaningOrganizersDto.builder()
                                                                    .list(List.of(CleaningOrganizeDto.builder().rooms(21).jrNumber(2).srNumber(1).build(),
                                                                                  CleaningOrganizeDto.builder().rooms(35).jrNumber(1).srNumber(3).build(),
                                                                                  CleaningOrganizeDto.builder().rooms(17).jrNumber(0).srNumber(2).build(),
                                                                                  CleaningOrganizeDto.builder().rooms(28).jrNumber(3).srNumber(1).build()))
                                                                    .build();

        //When/Then
        webTestClient.post()
                     .uri(URL)
                     .accept(MediaType.APPLICATION_JSON)
                     .bodyValue(request)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(CleaningOrganizersDto.class)
                     .isEqualTo(expected);
    }

    /*
     * In: { “rooms”: [24, 28], “senior”: 11, “junior”: 6 }
     * Out: [ {senior: 2, junior: 1}, {senior: 2, junior: 1} ]
     * */
    @Test
    public void shouldOrganizeCase2() {

        //Given
        final PortfolioInfoDto request = PortfolioInfoDto.builder().jrCapacity(6).srCapacity(11).structures(List.of(24, 28)).build();

        final CleaningOrganizersDto expected = CleaningOrganizersDto.builder()
                                                                    .list(List.of(CleaningOrganizeDto.builder().rooms(24).jrNumber(1).srNumber(2).build(),
                                                                                  CleaningOrganizeDto.builder().rooms(28).jrNumber(1).srNumber(2).build()))
                                                                    .build();

        //When/Then
        webTestClient.post()
                     .uri(URL)
                     .accept(MediaType.APPLICATION_JSON)
                     .bodyValue(request)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(CleaningOrganizersDto.class)
                     .isEqualTo(expected);
    }

    @Test
    public void shouldFailWithNoSrCapacity() {

        //Given
        final PortfolioInfoDto request = PortfolioInfoDto.builder().jrCapacity(6).srCapacity(null).structures(List.of(24, 28)).build();

        //When/Then
        webTestClient.post().uri(URL).accept(MediaType.APPLICATION_JSON).bodyValue(request).exchange().expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldFailWithNoJrCapacity() {

        //Given
        final PortfolioInfoDto request = PortfolioInfoDto.builder().jrCapacity(null).srCapacity(10).structures(List.of(24, 28)).build();

        //When/Then
        webTestClient.post().uri(URL).accept(MediaType.APPLICATION_JSON).bodyValue(request).exchange().expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldFailWithNoStructures() {

        //Given
        final PortfolioInfoDto request = PortfolioInfoDto.builder().jrCapacity(null).srCapacity(10).structures(Collections.emptyList()).build();


        //When/Then
        webTestClient.post().uri(URL).accept(MediaType.APPLICATION_JSON).bodyValue(request).exchange().expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldFailWithZeroSrCapacity() {

        //Given
        final PortfolioInfoDto request = PortfolioInfoDto.builder().jrCapacity(10).srCapacity(0).structures(Collections.emptyList()).build();


        //When/Then
        webTestClient.post().uri(URL).accept(MediaType.APPLICATION_JSON).bodyValue(request).exchange().expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldFailWithOneBuildingWithMoreThan100Rooms() {

        //Given
        final PortfolioInfoDto request = PortfolioInfoDto.builder().jrCapacity(10).srCapacity(1).structures(List.of(124)).build();


        //When/Then
        webTestClient.post().uri(URL).accept(MediaType.APPLICATION_JSON).bodyValue(request).exchange().expectStatus().isEqualTo(HttpStatus.PRECONDITION_FAILED);
    }

    @Test
    public void shouldFailWithJrCapacityMoreThanStCapacity() {

        //Given
        final PortfolioInfoDto request = PortfolioInfoDto.builder().jrCapacity(10).srCapacity(1).structures(List.of(24)).build();


        //When/Then
        webTestClient.post().uri(URL).accept(MediaType.APPLICATION_JSON).bodyValue(request).exchange().expectStatus().isEqualTo(HttpStatus.PRECONDITION_FAILED);
    }

}
