package com.spo.challenge.service;

import com.spo.challenge.dto.CleaningOrganizeDto;
import com.spo.challenge.dto.CleaningOrganizersDto;
import com.spo.challenge.dto.PortfolioInfoDto;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import reactor.test.StepVerifier;

public class PortfolioOrganizerCraftUnitTest {

    private PortfolioOrganizer underTest;

    @Before
    public void init() {
        underTest = new PortfolioOrganizerCraft();
    }

    @Test
    public void shouldOrganizeCase1() {

        //Given
        final PortfolioInfoDto input = PortfolioInfoDto.builder().jrCapacity(6).srCapacity(10).structures(List.of(21, 35, 17, 28)).build();

        final CleaningOrganizersDto expected = CleaningOrganizersDto.builder()
                                                                    .list(List.of(CleaningOrganizeDto.builder().rooms(21).jrNumber(2).srNumber(1).build(),
                                                                                  CleaningOrganizeDto.builder().rooms(35).jrNumber(1).srNumber(3).build(),
                                                                                  CleaningOrganizeDto.builder().rooms(17).jrNumber(0).srNumber(2).build(),
                                                                                  CleaningOrganizeDto.builder().rooms(28).jrNumber(3).srNumber(1).build()))
                                                                    .build();

        //When/Test

        StepVerifier.create(underTest.organize(input)).expectNext(expected).expectComplete().verify();

    }

    @Test
    public void shouldOrganizeCase2() {

        //Given
        final PortfolioInfoDto input = PortfolioInfoDto.builder().jrCapacity(6).srCapacity(11).structures(List.of(24, 28)).build();

        final CleaningOrganizersDto expected = CleaningOrganizersDto.builder()
                                                                    .list(List.of(CleaningOrganizeDto.builder().rooms(24).jrNumber(1).srNumber(2).build(),
                                                                                  CleaningOrganizeDto.builder().rooms(28).jrNumber(1).srNumber(2).build()))
                                                                    .build();

        //When/Then
        StepVerifier.create(underTest.organize(input)).expectNext(expected).expectComplete().verify();
    }

}
