package com.codegen.hms.dto;

import com.codegen.hms.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomTypeRequestDTO {

    private Long id;
    private Hotel hotel;
    private String name;
    private Integer noOfRooms;
    private Integer maxAdults;
    private Float price;
}
