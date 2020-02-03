package com.spo.challenge.service;

import com.spo.challenge.dto.CleaningOrganizersDto;
import com.spo.challenge.dto.PortfolioInfoDto;
import reactor.core.publisher.Mono;

public interface PortfolioOrganizer {

    Mono<CleaningOrganizersDto> organize(PortfolioInfoDto portfolioInfoDto);
}
