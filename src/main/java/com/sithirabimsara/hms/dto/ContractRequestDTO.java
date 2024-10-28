package com.sithirabimsara.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContractRequestDTO {

    private Long id;
    private HotelRequestDTO hotel;
    private LocalDate startDate;
    private LocalDate endDate;
    private Float markup;

}
