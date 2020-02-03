package com.spo.challenge.controller;

import com.spo.challenge.dto.CleaningOrganizersDto;
import com.spo.challenge.dto.PortfolioInfoDto;
import com.spo.challenge.manager.PortfolioManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/organize/portfolios")
@AllArgsConstructor
public class PortfolioController {

    private final PortfolioManager portfolioManager;

    @ApiOperation(value = "OrganizePortfolio")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success organizing the Sr and Jr cleaners workforce per building", response = CleaningOrganizersDto.class),
            @ApiResponse(code = 400, message = "Bad Request Error"),
            @ApiResponse(code = 412, message = "Validation Error when: at least 1 item in the structures list has more than 100 rooms or the jr Cleaning Capacity is more than the Sr Cleaning Capacity"),
    })
    @PostMapping
    public ResponseEntity<Mono<CleaningOrganizersDto>> organizePortfolio(@RequestBody @Validated final PortfolioInfoDto portfolioInfoDto) {

        return ResponseEntity.ok(portfolioManager.organize(portfolioInfoDto));
    }
}
