package com.spo.challenge.service;

import com.spo.challenge.dto.CleaningOrganizeDto;
import com.spo.challenge.dto.CleaningOrganizersDto;
import com.spo.challenge.dto.PortfolioInfoDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PortfolioOrganizerCraft implements PortfolioOrganizer {

    @Override
    public Mono<CleaningOrganizersDto> organize(final PortfolioInfoDto portfolioInfoDto) {

        final List<Integer> structures = portfolioInfoDto.getStructures();
        final Integer jrCapacity = portfolioInfoDto.getJrCapacity();
        final Integer srCapacity = portfolioInfoDto.getSrCapacity();

        List<CleaningOrganizeDto> results = new ArrayList<>();
        structures.forEach(rooms -> results.add(organizeBuilding(jrCapacity, srCapacity, rooms)));

        return Mono.just(CleaningOrganizersDto.builder().list(results).build());
    }

    private CleaningOrganizeDto organizeBuilding(final Integer jrCapacity, final Integer srCapacity, final Integer rooms) {

        final CleaningOrganizeDto solutionWithSr = calculateSolutionWithSr(jrCapacity, srCapacity, rooms);
        final CleaningOrganizeDto solutionWithJr = calculateSolutionWithJr(jrCapacity, srCapacity, rooms);

        return chooseBestSolution(jrCapacity, srCapacity, rooms, solutionWithSr, solutionWithJr);
    }

    private CleaningOrganizeDto calculateSolutionWithJr(final Integer jrCapacity, final Integer srCapacity, final Integer rooms) {

        final int jrNeededToCleanBuilding = rooms / jrCapacity;
        final int roomsLeftOverByJr = rooms % jrCapacity;

        if (roomsLeftOverByJr > jrCapacity) {
            return CleaningOrganizeDto.builder().rooms(rooms).jrNumber(jrNeededToCleanBuilding).srNumber(1).build();
        }

        final int jrSrRatio = srCapacity / jrCapacity;
        final int numberJrNeededToFinishTheJob = jrNeededToCleanBuilding - jrSrRatio;

        return CleaningOrganizeDto.builder().rooms(rooms).jrNumber(numberJrNeededToFinishTheJob).srNumber(1).build();
    }

    private CleaningOrganizeDto calculateSolutionWithSr(final Integer jrCapacity, final Integer srCapacity, final Integer rooms) {

        final int srNeededToCleanBuilding = rooms / srCapacity;
        final int roomsLeftOverBySr = rooms % srCapacity;

        if (roomsLeftOverBySr <= jrCapacity) {
            return CleaningOrganizeDto.builder().rooms(rooms).jrNumber(1).srNumber(srNeededToCleanBuilding).build();
        }

        return CleaningOrganizeDto.builder().rooms(rooms).jrNumber(0).srNumber(srNeededToCleanBuilding + 1).build();
    }

    private CleaningOrganizeDto chooseBestSolution(final Integer jrCapacity,
                                                   final Integer srCapacity,
                                                   final Integer rooms,
                                                   final CleaningOrganizeDto solutionWithSr,
                                                   final CleaningOrganizeDto solutionWithJr) {

        int workForceFromSrSolution = calculateWorkForce(jrCapacity, srCapacity, solutionWithSr);
        int workForceFromJrSolution = calculateWorkForce(jrCapacity, srCapacity, solutionWithJr);

        if (workForceFromSrSolution < rooms || workForceFromJrSolution < rooms) {
            return workForceFromSrSolution < rooms ? solutionWithJr : solutionWithSr;
        }

        return workForceFromSrSolution > workForceFromJrSolution ? solutionWithJr : solutionWithSr;
    }

    private int calculateWorkForce(final Integer jrCapacity, final Integer srCapacity, final CleaningOrganizeDto solution) {
        final int jrNumber = solution.getJrNumber();
        final int srNumber = solution.getSrNumber();
        return (jrNumber * jrCapacity) + (srNumber * srCapacity);
    }
}
