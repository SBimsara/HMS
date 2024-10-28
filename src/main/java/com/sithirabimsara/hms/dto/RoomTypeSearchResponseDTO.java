package com.sithirabimsara.hms.dto;

import com.sithirabimsara.hms.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data

public class RoomTypeSearchResponseDTO {

    private Long id;
    private String name;
    private Integer noOfRooms;
    private Integer maxAdults;
    private Float price;
    private Hotel hotel;
}
