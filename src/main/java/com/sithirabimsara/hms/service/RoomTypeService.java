package com.sithirabimsara.hms.service;

import com.sithirabimsara.hms.dto.RoomTypeResponseDTO;
import com.sithirabimsara.hms.dto.RoomTypeSearchResponseDTO;
import com.sithirabimsara.hms.entity.RoomType;
import com.sithirabimsara.hms.exception.DataNotFoundException;
import com.sithirabimsara.hms.repo.RoomTypeRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomTypeService {

    @Autowired
    private RoomTypeRepo roomTypeRepo;

    @Autowired
    private ModelMapper modelMapper;

    public RoomType addRoomType(RoomType roomType) {
        return roomTypeRepo.save(roomType);
    }

    public List<RoomTypeResponseDTO> getRoomTypes() {

        List<RoomType> roomTypesList = roomTypeRepo.findAll();
        return modelMapper.map(roomTypesList, new TypeToken<List<RoomTypeResponseDTO>>(){}.getType());
    }
    public RoomType updateRoomtype(RoomType roomType) {

        if(roomType.getId() != null){
            Long roomTypeId = roomType.getId();
            RoomType existingRoomType = roomTypeRepo.findById(roomTypeId).orElseThrow(() -> new DataNotFoundException("Room Type by ID :"+roomTypeId+ " was not found."));

            existingRoomType.setName(roomType.getName());
            existingRoomType.setHotel(roomType.getHotel());
            existingRoomType.setNoOfRooms(roomType.getNoOfRooms());
            existingRoomType.setMaxAdults(roomType.getMaxAdults());
            existingRoomType.setPrice(roomType.getPrice());

            return roomTypeRepo.save(existingRoomType);
        }
        else{
            return roomTypeRepo.save(roomType);
        }

    }

    public void deleteRoomType (Long roomTypeId) {
        if(roomTypeRepo.existsById(roomTypeId)){
            roomTypeRepo.deleteById(roomTypeId);
        }
        else {
            throw new DataNotFoundException("Room Type by" + roomTypeId + " was not found.");
        }

    }

    public RoomType getContractById(Long id) {
        RoomType roomType = roomTypeRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Room Type by " + id + " was not found."));
        return roomType;
    }

    public List<RoomTypeSearchResponseDTO> searchRoomTypes(Integer noOfAdults){
        List<RoomType> searchRoomTypesList = roomTypeRepo.findByMaxAdultsGreaterThanEqualAndHotelIsNotNull(noOfAdults);
        return modelMapper.map(searchRoomTypesList, new TypeToken<List<RoomTypeSearchResponseDTO>>(){}.getType());
    }

}
