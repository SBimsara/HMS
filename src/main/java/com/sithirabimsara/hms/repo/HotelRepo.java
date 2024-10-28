package com.sithirabimsara.hms.repo;

import com.sithirabimsara.hms.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HotelRepo extends JpaRepository<Hotel, Long> {

    Boolean existsByName (String name);

    Hotel findByName (String name);
}
