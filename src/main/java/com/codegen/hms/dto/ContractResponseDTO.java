package com.codegen.hms.dto;

import com.codegen.hms.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ContractResponseDTO {

    private Long id;
    private HotelResponseDTO hotel;
    private LocalDate startDate;
    private LocalDate endDate;
    private Float markup;
}
