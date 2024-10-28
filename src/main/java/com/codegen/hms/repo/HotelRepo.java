package com.codegen.hms.repo;

import com.codegen.hms.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface HotelRepo extends JpaRepository<Hotel, Long> {

    Boolean existsByName (String name);

    Hotel findByName (String name);
}
