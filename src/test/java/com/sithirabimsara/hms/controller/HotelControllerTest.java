package com.sithirabimsara.hms.controller;

import com.sithirabimsara.hms.service.HotelService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    @Test
    public void deleteHotel_ReturnsHttpStatusOK() {
        // Arrange
        Long hotelId = 1L;

        // Act
        ResponseEntity<?> responseEntity = hotelController.deleteHotel(hotelId);

        // Assert
        // Verify that deleteHotel method is called with the correct argument
        verify(hotelService).deleteHotel(hotelId);

        // Verify that the response entity has HTTP status OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}