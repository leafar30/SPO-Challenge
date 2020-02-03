package com.spo.challenge.dto;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PortfolioInfoDto {

    @NotNull(message = "srCapacity is required")
    @Min(value = 1, message = "srCapacity must be more than 0")
    private final Integer srCapacity;

    @NotNull(message = "jrCapacity is required")
    @Min(value = 1, message = "jrCapacity must be more than 0")
    private final Integer jrCapacity;

    @NotNull(message = "Structures list is required")
    @Size(min = 1, max = 100, message = "Min size 1, Max size 100")
    private final List<Integer> structures;
}
