package com.codegen.hms.repo;

import com.codegen.hms.HMSApplication;
import com.codegen.hms.entity.Contract;
import com.codegen.hms.entity.Hotel;
import com.codegen.hms.entity.RoomType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {HMSApplication.class})
public class ContractRepoTest {


    @Mock
    private ContractRepo contractRepo;


    @Test
    public void findByStartDateLessThanEqualAndEndDateGreaterThanEqual_ReturnsContracts() {
        // Arrange
        LocalDate checkInDate = LocalDate.of(2024, 9, 3);
        LocalDate checkOutDate = LocalDate.of(2024, 9, 6);

        RoomType roomType1 = new RoomType();
        roomType1.setId(1L);
        roomType1.setName("First Class");
        roomType1.setNoOfRooms(5);
        roomType1.setMaxAdults(2);
        roomType1.setPrice(10000.0f);

        RoomType roomType2 = new RoomType();
        roomType1.setId(2L);
        roomType1.setName("Second Class");
        roomType1.setNoOfRooms(6);
        roomType1.setMaxAdults(2);
        roomType1.setPrice(7000.0f);

        List<RoomType> roomTypeList = new ArrayList<>();
        roomTypeList.add(roomType1);
        roomTypeList.add(roomType2);

        Hotel hotel1 = Hotel.builder()
                .id(1L)
                .name("Cinnamon Grand")
                .roomTypes(roomTypeList)
                .build();

        Hotel hotel2 = Hotel.builder()
                .id(2L)
                .name("Shangrla")
                .roomTypes(roomTypeList)
                .build();

        Hotel hotel3 = Hotel.builder()
                .id(3L)
                .name("Marriot")
                .roomTypes(roomTypeList)
                .build();

        Contract contract1 = Contract.builder()
                .id(1L)
                .hotel(hotel1)
                .startDate(LocalDate.of(2024, 1, 30))
                .endDate(LocalDate.of(2024, 7, 30))
                .markup(10.0f)
                .build();

        Contract contract2 = Contract.builder()
                .id(2L)
                .hotel(hotel2)
                .startDate(LocalDate.of(2024, 2, 28))
                .endDate(LocalDate.of(2024, 7, 30))
                .markup(15.0f)
                .build();

        Contract contract3 = Contract.builder()
                .id(3L)
                .hotel(hotel3)
                .startDate(LocalDate.of(2024, 1, 31))
                .endDate(LocalDate.of(2024, 12, 31))
                .markup(20.0f)
                .build();


        List<Contract> contracts = Arrays.asList(contract3);

        // Mock the behavior of the repository
        when(contractRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(checkInDate, checkOutDate))
                .thenReturn(contracts);

        // Act
        List<Contract> result = contractRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(checkInDate, checkOutDate);

        // Assert
        assertEquals(1, result.size());

        // Verify that the method was called with the correct parameters
        verify(contractRepo, times(1)).findByStartDateLessThanEqualAndEndDateGreaterThanEqual(checkInDate, checkOutDate);
    }



}