package com.sithirabimsara.hms.service;

import com.sithirabimsara.hms.dto.HotelRequestDTO;
import com.sithirabimsara.hms.dto.RoomTypeRequestDTO;
import com.sithirabimsara.hms.entity.Hotel;
import com.sithirabimsara.hms.entity.RoomType;
import com.sithirabimsara.hms.exception.DataNotFoundException;
import com.sithirabimsara.hms.exception.InvalidInputException;
import com.sithirabimsara.hms.exception.NullInputException;
import com.sithirabimsara.hms.repo.HotelRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HotelServiceTest {
    @Mock
    private HotelRepo hotelRepo;

    @Mock
    private RoomTypeService roomTypeService;

    @InjectMocks
    private HotelService hotelService;

    public HotelServiceTest() {
        MockitoAnnotations.initMocks(this); // Initialize mocks before each test
    }

    @Test
    public void deleteHotel_Success() {
        // Arrange
        Long hotelId = 1L;
        when(hotelRepo.existsById(hotelId)).thenReturn(true);

        // Act
        hotelService.deleteHotel(hotelId);

        // Assert
        verify(hotelRepo, times(1)).deleteById(hotelId);
    }

    @Test
    public void deleteHotel_ThrowsDataNotFoundException() {
        // Arrange
        Long hotelId = 2L;
        when(hotelRepo.existsById(hotelId)).thenReturn(false);

        // Act and Assert
        assertThrows(DataNotFoundException.class, () -> hotelService.deleteHotel(hotelId));
    }

    @Test
    public void addHotelWithRoomTypes_ThrowsInvalidInputException() {

        // Arrange
        RoomTypeRequestDTO roomType1 = new RoomTypeRequestDTO();
        roomType1.setId(1L);
        roomType1.setName("First Class");
        roomType1.setNoOfRooms(5);
        roomType1.setMaxAdults(2);
        roomType1.setPrice(10000.0f);

        List<RoomTypeRequestDTO> roomTypeList = new ArrayList<>();
        roomTypeList.add(roomType1);

        HotelRequestDTO hotelRequestDTO = new HotelRequestDTO(1L, "Cinnamon Grand", roomTypeList);

        // Assume the hotel already exists
        when(hotelRepo.existsByName(hotelRequestDTO.getName())).thenReturn(true);

        // Act and Assert
        assertThrows(InvalidInputException.class, () -> {
            hotelService.addHotelWithRoomTypes(hotelRequestDTO);
        });

        // Verify interactions with mock objects
        verify(hotelRepo, times(1)).existsByName(hotelRequestDTO.getName());
        verify(hotelRepo, never()).save(any(Hotel.class));
        verify(roomTypeService, never()).addRoomType(any(RoomType.class));
    }

    @Test
    public void updateHotelWithRoomTypes_ThrowsDataNotFoundException() {

        // Arrange
        Long hotelId = 1L;

        RoomTypeRequestDTO roomType1 = new RoomTypeRequestDTO();
        roomType1.setId(1L);
        roomType1.setName("First Class");
        roomType1.setNoOfRooms(5);
        roomType1.setMaxAdults(2);
        roomType1.setPrice(10000.0f);

        List<RoomTypeRequestDTO> roomTypeList = new ArrayList<>();
        roomTypeList.add(roomType1);

        HotelRequestDTO hotelRequestDTO = new HotelRequestDTO(1L, "Cinnamon Grand", roomTypeList);

        when(hotelRepo.existsById(hotelId)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> hotelService.updateHotelWithRoomTypes(hotelRequestDTO) );


    }

    @Test
    public void updateHotelWithRoomTypes_ThrowsNullInputException() {

        // Arrange
        HotelRequestDTO hotelRequestDTO = new HotelRequestDTO();
        hotelRequestDTO.setId(null);

        // Act & Assert
        try {
            hotelService.updateHotelWithRoomTypes(hotelRequestDTO);
        } catch (NullInputException e) {
            return;
        }

        // Fail the test if NullInputException is not thrown
        fail("Expected NullInputException, but no exception was thrown.");
    }






}