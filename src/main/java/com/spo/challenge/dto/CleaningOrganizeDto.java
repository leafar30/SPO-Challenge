package com.spo.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CleaningOrganizeDto {

    private final int rooms;
    private final int srNumber;
    private final int jrNumber;
}
