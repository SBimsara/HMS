package com.sithirabimsara.hms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "contract_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @NotNull(message = "Start date cannot be null")
    //@FutureOrPresent(message = "Start date should be in present or future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date should be in future")
    private LocalDate endDate;

    @NotNull(message = "Markup value cannot be null")
    @Positive(message = "Price should be greater than zero")
    private Float markup;

    private boolean isExpired = false;
}
