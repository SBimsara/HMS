package com.codegen.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "room_type")
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "room_type_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @NotBlank(message = "Room Type name cannot be empty")
    private String name;

    @NotNull(message = "No of Rooms cannot be null")
    @Positive(message = "No. of Rooms should be greater than zero")
    private Integer noOfRooms;

    @NotNull(message = "Max adults cannot be null")
    @Positive(message = "No. of Rooms should be greater than zero")
    private Integer maxAdults;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price should be greater than zero")
    private Float price;

}
