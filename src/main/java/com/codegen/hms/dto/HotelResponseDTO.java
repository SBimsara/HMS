package com.codegen.hms.dto;

import com.codegen.hms.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HotelResponseDTO {

    private Long id;
    private String name;
    private List<RoomTypeResponseDTO> roomTypes;
}
