package com.sithirabimsara.hms.repo;

import com.sithirabimsara.hms.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface ContractRepo extends JpaRepository<Contract, Long> {

    List<Contract> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate checkInDate, LocalDate checkOutDate);
}
