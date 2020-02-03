package com.spo.challenge.manager;

import com.spo.challenge.dto.CleaningOrganizersDto;
import com.spo.challenge.dto.PortfolioInfoDto;
import com.spo.challenge.exception.ValueNotAcceptedException;
import com.spo.challenge.service.PortfolioOrganizer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import static java.lang.String.format;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioManager {

    private static final String ROOMS_NUMBER_ERROR_MSG = "Rooms Number per Building cannot be more than [%d]";
    private static final String CAPACITY_ERROR_MSG = "Jr Cleaning capacity [%d] cannot be >= than Sr Cleaning Capacity [%d]";

    @Value(value = "${cleaning.rooms.max}")
    private Integer maxRoomNumber;

    @Autowired
    private PortfolioOrganizer portfolioOrganizer;

    public Mono<CleaningOrganizersDto> organize(final PortfolioInfoDto portfolioInfoDto) {
        portfolioInfoDto.getStructures().forEach(this::validateMaxRoomsNumber);
        validateCapacities(portfolioInfoDto);
        return portfolioOrganizer.organize(portfolioInfoDto);
    }

    private void validateMaxRoomsNumber(final Integer rooms) {
        if (rooms > 100) {
            final String msg = format(ROOMS_NUMBER_ERROR_MSG, maxRoomNumber);
            log.error(msg);
            throw new ValueNotAcceptedException(msg);
        }
    }

    private void validateCapacities(final PortfolioInfoDto portfolioInfoDto) {
        final Integer jrCapacity = portfolioInfoDto.getJrCapacity();
        final Integer srCapacity = portfolioInfoDto.getSrCapacity();
        if (jrCapacity >= srCapacity) {
            final String msg = format(CAPACITY_ERROR_MSG, srCapacity, jrCapacity);
            log.error(msg);
            throw new ValueNotAcceptedException(msg);
        }
    }
}
