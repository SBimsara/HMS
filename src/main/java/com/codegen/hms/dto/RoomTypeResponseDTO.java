package com.codegen.hms.dto;

import com.codegen.hms.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data

public class RoomTypeResponseDTO {

    private Long id;
    private String name;
    private Integer noOfRooms;
    private Integer maxAdults;
    private Float price;
}
