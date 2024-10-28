package com.codegen.hms.dto;

import com.codegen.hms.entity.Contract;
import com.codegen.hms.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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
