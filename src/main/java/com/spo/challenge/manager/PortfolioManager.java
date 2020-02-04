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


    /**
     * method used to run business validation and call the organizer logic if there are more implementations, will be the once in charge to choose the right
     * implementation
     *
     * @param portfolioInfoDto
     * @return
     */
    public Mono<CleaningOrganizersDto> organize(final PortfolioInfoDto portfolioInfoDto) {
        portfolioInfoDto.getStructures().forEach(this::validateMaxRoomsNumber);
        validateCapacities(portfolioInfoDto);
        return portfolioOrganizer.organize(portfolioInfoDto);
    }

    /**
     * validate maximum number of rooms per building
     * @param rooms
     */
    private void validateMaxRoomsNumber(final Integer rooms) {
        if (rooms > 100) {
            final String msg = format(ROOMS_NUMBER_ERROR_MSG, maxRoomNumber);
            log.error(msg);
            throw new ValueNotAcceptedException(msg);
        }
    }

    /**
     * validate the jr vs Sr capacitiesL the Jr capacity should not be more than
     * the Sr capacity
     * @param portfolioInfoDto
     */
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
