package com.codegen.hms.dto;

import com.codegen.hms.entity.RoomType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelRequestDTO {

    private Long id;
    private String name;
    private List<RoomTypeRequestDTO> roomTypes;
}
