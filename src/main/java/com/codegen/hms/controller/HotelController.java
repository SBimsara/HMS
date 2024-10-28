package com.codegen.hms.controller;

import com.codegen.hms.dto.HotelRequestDTO;
import com.codegen.hms.dto.HotelResponseDTO;
import com.codegen.hms.entity.Hotel;
import com.codegen.hms.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/hotel")
@CrossOrigin(origins = "*")
public class HotelController {

    @Autowired
    private HotelService hotelService;

//    @PostMapping("/save")
//    public ResponseEntity<Hotel> addHotel(@RequestBody HotelRequestDTO hotelRequestDTO) {
//        Hotel newHotel = hotelService.addHotelWithRoomTypes(hotelRequestDTO);
//        return new ResponseEntity<>(newHotel,HttpStatus.CREATED);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<HotelResponseDTO>> getAllHotels() {
        List<HotelResponseDTO> hotels = hotelService.getHotels();
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Hotel> updateHotel(@Valid @RequestBody HotelRequestDTO hotelRequestDTO) {
        Hotel updatedHotel = hotelService.updateHotelWithRoomTypes(hotelRequestDTO);
        return new ResponseEntity<>(updatedHotel, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable("id") Long id){
        hotelService.deleteHotel(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
