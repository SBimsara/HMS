package com.codegen.hms.repo;

import com.codegen.hms.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public interface ContractRepo extends JpaRepository<Contract, Long> {

    List<Contract> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate checkInDate, LocalDate checkOutDate);
}
