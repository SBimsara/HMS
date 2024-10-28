package com.codegen.hms.repo;

import com.codegen.hms.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface RoomTypeRepo extends JpaRepository<RoomType, Long> {


    List<RoomType> findByMaxAdultsGreaterThanEqualAndHotelIsNotNull(Integer noOfAdults);

    List<RoomType> findByNoOfRoomsGreaterThanEqualAndMaxAdultsGreaterThanEqualAndHotelIsNotNull(Integer noOfRooms,Integer noOfAdults);
}
