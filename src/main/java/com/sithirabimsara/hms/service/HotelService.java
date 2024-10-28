package com.sithirabimsara.hms.service;

import com.sithirabimsara.hms.dto.HotelRequestDTO;
import com.sithirabimsara.hms.dto.HotelResponseDTO;
import com.sithirabimsara.hms.dto.RoomTypeRequestDTO;
import com.sithirabimsara.hms.entity.Hotel;
import com.sithirabimsara.hms.entity.RoomType;
import com.sithirabimsara.hms.exception.DataNotFoundException;
import com.sithirabimsara.hms.exception.InvalidInputException;
import com.sithirabimsara.hms.exception.NullInputException;
import com.sithirabimsara.hms.repo.HotelRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class HotelService {

    @Autowired
    private HotelRepo hotelRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoomTypeService roomTypeService;

    static Logger log = Logger.getLogger(HotelService.class.getName());


    public Hotel addHotel(HotelRequestDTO hotelRequestDTO) {
        Hotel newHotel = Hotel.builder()
                .name(hotelRequestDTO.getName())
                .roomTypes(modelMapper.map(hotelRequestDTO.getRoomTypes() , new TypeToken<List<RoomTypeRequestDTO>>(){}.getType()))
                .build();

        return hotelRepo.save(newHotel);

    }

    public Hotel addHotelWithRoomTypes(HotelRequestDTO hotelRequestDTO) {

        if(hotelRepo.existsByName(hotelRequestDTO.getName())){
            throw new InvalidInputException("The hotel name must be UNIQUE.");
        }
        log.info("Attempting to create a new Hotel.");
        Hotel newHotel = Hotel.builder()
                .name(hotelRequestDTO.getName())
//                .roomTypes(modelMapper.map(hotelRequestDTO.getRoomTypes() , new TypeToken<List<RoomTypeRequestDTO>>(){}.getType()))
                .build();


        Hotel savedHotel = hotelRepo.save(newHotel);
        log.info("Hotel saved successfully. Adding Room Types");
        for (RoomTypeRequestDTO roomTypeRequestDTO : hotelRequestDTO.getRoomTypes()) {
            RoomType newRoomType = RoomType.builder()
                    .name(roomTypeRequestDTO.getName())
                    .hotel(savedHotel)
                    .noOfRooms(roomTypeRequestDTO.getNoOfRooms())
                    .maxAdults(roomTypeRequestDTO.getMaxAdults())
                    .price(roomTypeRequestDTO.getPrice())
                    .build();
            roomTypeService.addRoomType(newRoomType);

            log.info("Room Type added: " + newRoomType.getName());
        }

        log.info("All Room Types added successfully. Returning the saved Hotel");

        return savedHotel;
    }

    public Hotel updateHotelWithRoomTypes(HotelRequestDTO hotelRequestDTO) {

        log.info("Verifying the validity of the Hotel ID");
        if(hotelRequestDTO.getId() !=null){
            Long hotelId = hotelRequestDTO.getId();

            Hotel existingHotel = hotelRepo.findById(hotelId).orElseThrow(() -> new DataNotFoundException("Hotel by id " + hotelId + " was not found."));

            if(hotelRequestDTO.getRoomTypes().isEmpty()) {
                throw new InvalidInputException("Hotel must have Room Types.");
            }

            existingHotel.setName(hotelRequestDTO.getName());
            List<RoomType> existingRoomTypesList = new ArrayList<>(existingHotel.getRoomTypes());
            List<RoomType> newRoomTypesList = new ArrayList<>(modelMapper.map(hotelRequestDTO.getRoomTypes(), new TypeToken<List<RoomType>>(){}.getType()));

            for(RoomType existingRoomType : existingRoomTypesList){
                boolean foundMatch = false;
                for(RoomType newRoomType : newRoomTypesList) {
                    if(existingRoomType.getId()==newRoomType.getId()){
                        foundMatch = true;
                        break;
                    }
                }
                if (!foundMatch) {
                    roomTypeService.deleteRoomType(existingRoomType.getId());
                }
            }

            Hotel updatedHotel = hotelRepo.save(existingHotel);

            for (RoomTypeRequestDTO roomTypeRequestDTO : hotelRequestDTO.getRoomTypes()) {
                RoomType roomType = modelMapper.map(roomTypeRequestDTO, new TypeToken<RoomType>(){}.getType());
                roomType.setHotel(updatedHotel);

                roomTypeService.updateRoomtype(roomType);
            }
            return updatedHotel;
        }
        else{
            throw new NullInputException("hotel_id field cannot be NULL");
        }

    }

    public List<HotelResponseDTO> getHotels() {
        List<Hotel> hotelList = hotelRepo.findAll();
        return modelMapper.map(hotelList,new TypeToken<List<HotelResponseDTO>>(){}.getType());
    }

    public void deleteHotel(Long id) {
        if(hotelRepo.existsById(id)){
            hotelRepo.deleteById(id);
        }
        else{
            throw new DataNotFoundException("Hotel by "+ id+ " was not found.");
        }

    }


}
