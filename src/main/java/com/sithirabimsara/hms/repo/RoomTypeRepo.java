package com.sithirabimsara.hms.repo;

import com.sithirabimsara.hms.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoomTypeRepo extends JpaRepository<RoomType, Long> {


    List<RoomType> findByMaxAdultsGreaterThanEqualAndHotelIsNotNull(Integer noOfAdults);

    List<RoomType> findByNoOfRoomsGreaterThanEqualAndMaxAdultsGreaterThanEqualAndHotelIsNotNull(Integer noOfRooms,Integer noOfAdults);
}
