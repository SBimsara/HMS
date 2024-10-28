package com.sithirabimsara.hms.controller;

import com.sithirabimsara.hms.dto.RoomTypeResponseDTO;
import com.sithirabimsara.hms.dto.RoomTypeSearchResponseDTO;
import com.sithirabimsara.hms.entity.RoomType;
import com.sithirabimsara.hms.service.RoomTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/roomtype")
@CrossOrigin(origins = "*")

public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/all")
    public ResponseEntity<List<RoomTypeResponseDTO>> getAllEmployees () {
        List<RoomTypeResponseDTO> roomTypes = roomTypeService.getRoomTypes();
        return new ResponseEntity<>(roomTypes, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<RoomType> saveRoomType(@Valid @RequestBody RoomType roomType) {
        RoomType newRoomType = roomTypeService.addRoomType(roomType);
        return new ResponseEntity<>(newRoomType, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<RoomType> updateRoomType(@Valid @RequestBody RoomType roomType) {
        RoomType updatedRoomType = roomTypeService.updateRoomtype(roomType);
        return new ResponseEntity<>(updatedRoomType, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoomTypeSearchResponseDTO>> searchRoomTypes (@RequestParam("noOfAdults") Integer noOfAdults){
        List<RoomTypeSearchResponseDTO> searchedRoomTypeList = roomTypeService.searchRoomTypes(noOfAdults);
        return new ResponseEntity<>(searchedRoomTypeList,HttpStatus.OK);
    }
}
