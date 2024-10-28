package com.sithirabimsara.hms.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchRequestDTO {

    private LocalDate checkInDate;
    private Integer noOfNights;
    private List<Integer> roomsRequired;
    private List<Integer> adultsPerRoom;

}
