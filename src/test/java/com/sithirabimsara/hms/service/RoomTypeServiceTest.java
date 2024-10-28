package com.sithirabimsara.hms.service;

import com.sithirabimsara.hms.entity.RoomType;
import com.sithirabimsara.hms.exception.DataNotFoundException;
import com.sithirabimsara.hms.repo.RoomTypeRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoomTypeServiceTest {

    @Mock
    private RoomTypeRepo roomTypeRepo;

    @InjectMocks
    private RoomTypeService roomTypeService;

    public RoomTypeServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addRoomType_Success() {
        // Arrange
        RoomType roomTypeToAdd = new RoomType();
        roomTypeToAdd.setId(1L);
        roomTypeToAdd.setName("Deluxe Room");
        roomTypeToAdd.setNoOfRooms(10);
        roomTypeToAdd.setMaxAdults(2);
        roomTypeToAdd.setPrice(15000.0f);

        when(roomTypeRepo.save(any(RoomType.class))).thenReturn(roomTypeToAdd);

        // Act
        RoomType addedRoomType = roomTypeService.addRoomType(roomTypeToAdd);

        // Assert
        assertNotNull(addedRoomType);
        assertEquals(roomTypeToAdd.getId(), addedRoomType.getId());
        assertEquals(roomTypeToAdd.getName(), addedRoomType.getName());
        assertEquals(roomTypeToAdd.getNoOfRooms(), addedRoomType.getNoOfRooms());
        assertEquals(roomTypeToAdd.getMaxAdults(), addedRoomType.getMaxAdults());
        assertEquals(roomTypeToAdd.getPrice(), addedRoomType.getPrice(), 0.001); // Use delta for float comparison

        // Verify that the save method was called with the expected RoomType
        verify(roomTypeRepo, times(1)).save(eq(roomTypeToAdd));
    }

    @Test
    public void updateRoomType_WithExistingId() {
        // Arrange
        Long existingRoomTypeId = 1L;

        RoomType existingRoomType = new RoomType();
        existingRoomType.setId(existingRoomTypeId);
        existingRoomType.setName("Standard Room");
        existingRoomType.setNoOfRooms(5);
        existingRoomType.setMaxAdults(2);
        existingRoomType.setPrice(10000.0f);

        RoomType updatedRoomType = new RoomType();
        updatedRoomType.setId(existingRoomTypeId);
        updatedRoomType.setName("Deluxe Room");
        updatedRoomType.setNoOfRooms(10);
        updatedRoomType.setMaxAdults(3);
        updatedRoomType.setPrice(15000.0f);

        when(roomTypeRepo.findById(existingRoomTypeId)).thenReturn(java.util.Optional.of(existingRoomType));
        when(roomTypeRepo.save(any(RoomType.class))).thenReturn(updatedRoomType);

        // Act
        RoomType result = roomTypeService.updateRoomtype(updatedRoomType);

        // Assert
        assertNotNull(result);
        assertEquals(existingRoomTypeId, result.getId());
        assertEquals(updatedRoomType.getName(), result.getName());
        assertEquals(updatedRoomType.getNoOfRooms(), result.getNoOfRooms());
        assertEquals(updatedRoomType.getMaxAdults(), result.getMaxAdults());
        assertEquals(updatedRoomType.getPrice(), result.getPrice(), 0.001); // Use delta for float comparison

        // Verify that findById and save methods were called
        verify(roomTypeRepo, times(1)).findById(existingRoomTypeId);
        verify(roomTypeRepo, times(1)).save(eq(existingRoomType));
    }

    @Test
    public void updateRoomType_ThrowsDataNotFoundException() {
        // Arrange
        Long nonExistingRoomTypeId = 1L;

        RoomType roomTypeToUpdate = new RoomType();
        roomTypeToUpdate.setId(nonExistingRoomTypeId);
        roomTypeToUpdate.setName("Deluxe Room");
        roomTypeToUpdate.setNoOfRooms(10);
        roomTypeToUpdate.setMaxAdults(2);
        roomTypeToUpdate.setPrice(15000.0f);

        when(roomTypeRepo.findById(nonExistingRoomTypeId)).thenReturn(java.util.Optional.empty());

        // Act and Assert
        assertThrows(DataNotFoundException.class, () -> roomTypeService.updateRoomtype(roomTypeToUpdate));

        // Verify that findById method was called
        verify(roomTypeRepo, times(1)).findById(eq(nonExistingRoomTypeId));
        verify(roomTypeRepo, times(0)).save(any(RoomType.class)); // save should not be called
    }

    @Test
    public void updateRoomType_WithoutExistingId() {
        // Arrange
        RoomType roomTypeWithoutId = new RoomType();
        roomTypeWithoutId.setName("Suite");
        roomTypeWithoutId.setNoOfRooms(8);
        roomTypeWithoutId.setMaxAdults(4);
        roomTypeWithoutId.setPrice(20000.0f);

        when(roomTypeRepo.save(any(RoomType.class))).thenReturn(roomTypeWithoutId);

        // Act
        RoomType result = roomTypeService.updateRoomtype(roomTypeWithoutId);

        // Assert
        assertNotNull(result);
        assertEquals(roomTypeWithoutId.getName(), result.getName());
        assertEquals(roomTypeWithoutId.getNoOfRooms(), result.getNoOfRooms());
        assertEquals(roomTypeWithoutId.getMaxAdults(), result.getMaxAdults());
        assertEquals(roomTypeWithoutId.getPrice(), result.getPrice(), 0.001); // Use delta for float comparison

        // Verify that save method was called
        verify(roomTypeRepo, times(0)).findById(anyLong()); // findById should not be called
        verify(roomTypeRepo, times(1)).save(eq(roomTypeWithoutId));
    }

    @Test
    public void deleteRoomType_Success() {
        // Arrange
        Long roomTypeId = 1L;
        Mockito.when(roomTypeRepo.existsById(roomTypeId)).thenReturn(true);

        // Act
        roomTypeService.deleteRoomType(roomTypeId);

        // Assert
        verify(roomTypeRepo, times(1)).deleteById(roomTypeId);
    }

    @Test
    public void deleteRoomType_ThrowsDataNotFoundException() {
        // Arrange
        Long roomTypeId = 2L;
        Mockito.when(roomTypeRepo.existsById(roomTypeId)).thenReturn(false);

        // Act and Assert
        assertThrows(DataNotFoundException.class, () -> roomTypeService.deleteRoomType(roomTypeId));
    }

}