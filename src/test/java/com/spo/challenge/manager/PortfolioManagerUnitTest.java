package com.spo.challenge.manager;


import com.spo.challenge.dto.CleaningOrganizeDto;
import com.spo.challenge.dto.CleaningOrganizersDto;
import com.spo.challenge.dto.PortfolioInfoDto;
import com.spo.challenge.exception.ValueNotAcceptedException;
import com.spo.challenge.service.PortfolioOrganizerCraft;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

public class PortfolioManagerUnitTest {

    private PortfolioOrganizerCraft portfolioOrganizer;

    private PortfolioManager underTest;

    @Before
    public void init() {
        portfolioOrganizer = Mockito.mock(PortfolioOrganizerCraft.class);
        underTest = new PortfolioManager(100, portfolioOrganizer);
    }

    @Test
    public void shouldFailWithRoomsValidationMoreThan100() {
        //Given
        final PortfolioInfoDto input = PortfolioInfoDto.builder().jrCapacity(10).srCapacity(10).structures(List.of(124)).build();

        //When/Then
        Assertions.assertThrows(ValueNotAcceptedException.class, () -> {
            underTest.organize(input);
        }, "Rooms Number per Building cannot be more than [100]");

    }

    @Test
    public void shouldFailWithCapacityValidation() {
        //Given
        final PortfolioInfoDto input = PortfolioInfoDto.builder().jrCapacity(10).srCapacity(0).structures(List.of(80)).build();

        //When/Then
        Assertions.assertThrows(ValueNotAcceptedException.class, () -> {
            underTest.organize(input);
        }, "Jr Cleaning capacity [10] cannot be >= than Sr Cleaning Capacity [0]");

    }

    @Test
    public void shouldFailWithCapacityValidationJrAndSrCapacityEquals() {
        //Given
        final PortfolioInfoDto input = PortfolioInfoDto.builder().jrCapacity(10).srCapacity(10).structures(List.of(80)).build();

        //When/Then
        Assertions.assertThrows(ValueNotAcceptedException.class, () -> {
            underTest.organize(input);
        }, "Jr Cleaning capacity [10] cannot be >= than Sr Cleaning Capacity [10]");
    }


    @Test
    public void shouldOrganize() {
        //Given
        final PortfolioInfoDto input = PortfolioInfoDto.builder().jrCapacity(10).srCapacity(11).structures(List.of(14)).build();

        final CleaningOrganizersDto expected = CleaningOrganizersDto.builder()
                                                                    .list(List.of(CleaningOrganizeDto.builder().rooms(24).jrNumber(1).srNumber(2).build()))
                                                                    .build();

        when(portfolioOrganizer.organize(input)).thenReturn(Mono.just(expected));


        //When/Then
        StepVerifier.create(underTest.organize(input)).expectNext(expected).expectComplete().verify();
    }
}
