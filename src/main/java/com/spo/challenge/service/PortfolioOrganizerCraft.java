package com.spo.challenge.service;

import com.spo.challenge.dto.CleaningOrganizeDto;
import com.spo.challenge.dto.CleaningOrganizersDto;
import com.spo.challenge.dto.PortfolioInfoDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 *
 */
@Service
public class PortfolioOrganizerCraft implements PortfolioOrganizer {

    /**
     * method to organize the workforce for cleaning all the buildings
     *
     * @param portfolioInfoDto
     * @return Mono<CleaningOrganizersDto>
     */
    @Override
    public Mono<CleaningOrganizersDto> organize(final PortfolioInfoDto portfolioInfoDto) {

        final List<Integer> structures = portfolioInfoDto.getStructures();
        final Integer jrCapacity = portfolioInfoDto.getJrCapacity();
        final Integer srCapacity = portfolioInfoDto.getSrCapacity();

        final List<CleaningOrganizeDto> results = structures.stream().map(rooms -> organizeBuilding(jrCapacity, srCapacity, rooms)).collect(Collectors.toList());

        return Mono.just(CleaningOrganizersDto.builder().list(results).build());
    }

    /**
     * method to organize the workforce for cleaning one buildings
     *
     * @param jrCapacity
     * @param srCapacity
     * @param rooms
     * @return CleaningOrganizeDto
     */
    private CleaningOrganizeDto organizeBuilding(final Integer jrCapacity, final Integer srCapacity, final Integer rooms) {

        final CleaningOrganizeDto solutionWithSr = calculateSolutionWithSr(jrCapacity, srCapacity, rooms);
        final CleaningOrganizeDto solutionWithJr = calculateSolutionWithJr(jrCapacity, srCapacity, rooms);

        return chooseBestSolution(jrCapacity, srCapacity, rooms, solutionWithSr, solutionWithJr);
    }

    /**
     * method to calculate the best workforce for cleaning one buildings using Jr as a mayor workforce
     *
     * @param jrCapacity
     * @param srCapacity
     * @param rooms
     * @return
     */
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

    /**
     *
     * method to calculate the best workforce for cleaning one buildings using Sr as a mayor workforce
     *
     * @param jrCapacity
     * @param srCapacity
     * @param rooms
     * @return
     */
    private CleaningOrganizeDto calculateSolutionWithSr(final Integer jrCapacity, final Integer srCapacity, final Integer rooms) {

        final int srNeededToCleanBuilding = rooms / srCapacity;
        final int roomsLeftOverBySr = rooms % srCapacity;

        if (roomsLeftOverBySr <= jrCapacity) {
            return CleaningOrganizeDto.builder().rooms(rooms).jrNumber(1).srNumber(srNeededToCleanBuilding).build();
        }

        return CleaningOrganizeDto.builder().rooms(rooms).jrNumber(0).srNumber(srNeededToCleanBuilding + 1).build();
    }

    /**
     *
     * method to calculate the best workforce between a solution with Jr as mayor workforce
     * and Sr as mayor workforce
     *
     * @param jrCapacity
     * @param srCapacity
     * @param rooms
     * @param solutionWithSr
     * @param solutionWithJr
     * @return
     */
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

    /**
     *
     * method to calculate workforce for a given cleaning solutions
     *
     * @param jrCapacity
     * @param srCapacity
     * @param solution
     * @return
     */
    private int calculateWorkForce(final Integer jrCapacity, final Integer srCapacity, final CleaningOrganizeDto solution) {
        final int jrNumber = solution.getJrNumber();
        final int srNumber = solution.getSrNumber();
        return (jrNumber * jrCapacity) + (srNumber * srCapacity);
    }
}
