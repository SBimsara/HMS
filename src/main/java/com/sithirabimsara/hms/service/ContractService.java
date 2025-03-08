package com.sithirabimsara.hms.service;

import com.sithirabimsara.hms.dto.*;
import com.sithirabimsara.hms.entity.Contract;
import com.sithirabimsara.hms.entity.Hotel;
import com.sithirabimsara.hms.entity.RoomType;
import com.sithirabimsara.hms.exception.DataNotFoundException;
import com.sithirabimsara.hms.exception.InvalidInputException;
import com.sithirabimsara.hms.exception.NullInputException;
import com.sithirabimsara.hms.repo.ContractRepo;
import com.sithirabimsara.hms.repo.RoomTypeRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContractService {

    @Autowired
    private ContractRepo contractRepo;

    @Autowired
    private RoomTypeRepo roomTypeRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HotelService hotelService;

    private static final long EXPIRED_DATE_CHECK_DELAY = 86400; // a day


    static Logger log = Logger.getLogger(ContractService.class.getName());


    public ContractResponseDTO addFullContact(ContractRequestDTO contractRequestDTO) {

        log.info("Attempting to save Hotel");

        Hotel savedHotel = hotelService.addHotelWithRoomTypes(contractRequestDTO.getHotel());

        log.info("Hotel saved successfully. Building the Contract");

        Contract newContract = Contract.builder()
                .hotel(savedHotel)
                .startDate(contractRequestDTO.getStartDate().plusDays(1))
                .endDate(contractRequestDTO.getEndDate().plusDays(1))
                .markup(contractRequestDTO.getMarkup())
                .build();
        log.info("Contract built successfully. Saving the Contract");
        Contract savedContract = contractRepo.save(newContract);
        return modelMapper.map(savedContract, new TypeToken<ContractResponseDTO>(){}.getType());
    }

    public ContractResponseDTO updateFullContract(ContractRequestDTO contractRequestDTO) {

        Long contractId = contractRequestDTO.getId();

        log.info("Verifying the existence of the contract");
        if(contractRepo.existsById(contractId)){

            HotelRequestDTO hotelRequestDTO = contractRequestDTO.getHotel();

            log.info("Contract exists. Updating the hotel.");
            Hotel updatedHotel = hotelService.updateHotelWithRoomTypes(hotelRequestDTO);

            Contract existingContract = contractRepo.findById(contractId).orElseThrow(() -> new DataNotFoundException("Contract by id "+ contractId+ " was not fond."));
            existingContract.setHotel(updatedHotel);
            existingContract.setStartDate(contractRequestDTO.getStartDate().plusDays(1));
            existingContract.setEndDate(contractRequestDTO.getEndDate().plusDays(1));
            existingContract.setMarkup(contractRequestDTO.getMarkup());

            Contract updatedContract = contractRepo.save(existingContract);
            return modelMapper.map(updatedContract, new TypeToken<ContractResponseDTO>(){}.getType());

        }
        else {
            throw new DataNotFoundException("Contract by "+ contractId + "was not found.");
        }

    }

    public void deleteFullContract(Long id) {
        contractRepo.deleteById(id);
    }

    public List<ContractResponseDTO> findAllContracts() {
        log.info("Attempting to retrieve all contracts.");
        List<Contract> contractList = contractRepo.findAll();
        log.info("Found "+ contractList.size() + " contracts.");
        return modelMapper.map(contractList,new TypeToken<List<ContractResponseDTO>>(){}.getType());
    }

    public List<ContractResponseDTO> getPagedContracts(int currentPage, int pageSize) {
        if(currentPage<0){
            throw new InvalidInputException("Page number must be greater than 0.");
        }
        else {
            currentPage = currentPage - 1;
        }
        if(pageSize<0){
            throw new InvalidInputException("Page size must be greater than 0.");
        }

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        log.info("Attempting to retrieve all contracts based on page.");
        Page<Contract> contracts = contractRepo.findAll(pageable);
        log.info("Contract retrieval based on pages successful");

        return contracts.getContent().stream()
                .map(contract -> modelMapper.map(contract, ContractResponseDTO.class))
                .collect(Collectors.toList());
    }


    public ContractResponseDTO getContractById(Long id) {
        Contract contract = contractRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Contract by "+ id+ " was not found."));
        return modelMapper.map(contract, new TypeToken<ContractResponseDTO>(){}.getType());
    }

    public List<ContractResponseDTO> searchAvailableContracts(LocalDate checkInDate, Integer noOfNights, Integer noOfAdults) {

        //calculating the check-out-date

        // Calculate check-out date by adding a certain number of days
        LocalDate checkOutDate = checkInDate.plus(noOfNights, ChronoUnit.DAYS);

        //getting the available contracts based on check-in-date and check-out-date
        List<Contract> contractsList = contractRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(checkInDate,checkOutDate);

        //getting the available room types based on no. of adults
        List<RoomType> roomTypesList = roomTypeRepo.findByMaxAdultsGreaterThanEqualAndHotelIsNotNull(noOfAdults);

        List<RoomType> matchingRoomTypesList = new ArrayList<>();
        List<ContractResponseDTO> matchingContractsList = new ArrayList<>();


        for(Contract contract: contractsList) {

            Hotel hotel = contract.getHotel();
            boolean matchFound = false;

            //selecting the room types that meet both requirements
            for (RoomType roomType : roomTypesList) {
                Long roomTypeId = roomType.getId();
                for (RoomType suitableRoomType : hotel.getRoomTypes()) {
                    if (roomTypeId.equals(suitableRoomType.getId())) {
                        matchingRoomTypesList.add(suitableRoomType);
                    }
                }
            }
            List<RoomTypeResponseDTO> newRoomTypeList = modelMapper.map(matchingRoomTypesList, new TypeToken<List<RoomTypeResponseDTO>>(){}.getType());

            //creating the contract on those selected room types
            if (!matchingRoomTypesList.isEmpty()) {
                ContractResponseDTO contractResponseDTO = ContractResponseDTO.builder()
                        .id(contract.getId())
                        .hotel(new HotelResponseDTO(hotel.getId(), hotel.getName(), newRoomTypeList))
                        .startDate(contract.getStartDate())
                        .endDate(contract.getEndDate())
                        .build();

                matchingContractsList.add(contractResponseDTO);
            }
        }
        return matchingContractsList;
    }

    public List<List<ContractResponseDTO>> searchContracts(SearchRequestDTO searchRequestDTO) {

        if((searchRequestDTO.getCheckInDate() == null) || (searchRequestDTO.getNoOfNights() == null)){
            throw new NullInputException("check-in-date and no. of nights must be NOT EMPTY.");
        }

        if((searchRequestDTO.getRoomsRequired().size() == searchRequestDTO.getAdultsPerRoom().size()) && (!searchRequestDTO.getRoomsRequired().isEmpty())){

            log.info("Search request validation successful: Number of rooms and number of adults lists have the same length.");
            log.info("Calculating check-out-date.");

            LocalDate checkInDate = searchRequestDTO.getCheckInDate().plusDays(1);
            Integer noOfNights = searchRequestDTO.getNoOfNights();

            //calculating the check-out-date
            LocalDate checkOutDate = checkInDate.plusDays(noOfNights + 1);

            log.info("Calculation successful. Fetching the available contracts based on check-in-date and check-out-date.");

            //getting the available contracts based on check-in-date and check-out-date
            List<Contract> contractsList = contractRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(checkInDate,checkOutDate);
            log.info("Fetching successful. Found contracts: "+contractsList.size());

            List<Integer> requiredRoomsList = searchRequestDTO.getRoomsRequired();
            List<Integer> adultsPerRoomList = searchRequestDTO.getAdultsPerRoom();


            //List<RoomType> availableRoomTypesList = new ArrayList<>();
            List<List<ContractResponseDTO>> finalList = new ArrayList<>();

            for(int i=0; i<= requiredRoomsList.size()-1 ; i++) {

                log.info("Started processing Room Type requirement for index: "+i);
                int requiredRooms = requiredRoomsList.get(i);
                int adultsPerRoom = adultsPerRoomList.get(i);

                List<ContractResponseDTO> matchingContractsList = new ArrayList<>();

                for(Contract suitableContract : contractsList) {

                    ContractResponseDTO suitableContractDTO = modelMapper.map(suitableContract, new TypeToken<ContractResponseDTO>(){}.getType());

                    List<RoomTypeResponseDTO> suitableRoomTypesList = suitableContractDTO.getHotel().getRoomTypes();
                    log.info(String.valueOf(suitableRoomTypesList.size()));
                    List<RoomTypeResponseDTO> availableRoomTypesDTOList = new ArrayList<>();


                    for(RoomTypeResponseDTO roomType : suitableRoomTypesList) {

                        if((roomType.getNoOfRooms()>=requiredRooms) && (roomType.getMaxAdults()>=adultsPerRoom)){
                            availableRoomTypesDTOList.add(roomType);
                        }
                        else{
                            continue;
                        }
                    }
                    log.info("Finished processing available Room Types for Contract index:  "+ contractsList.indexOf(suitableContract)+". Found Room Types: "+ availableRoomTypesDTOList.size());

                    if(!availableRoomTypesDTOList.isEmpty()) {

                        HotelResponseDTO hotel = suitableContractDTO.getHotel();
                        hotel.setRoomTypes(availableRoomTypesDTOList);

                        suitableContractDTO.setHotel(hotel);

                        matchingContractsList.add(suitableContractDTO);
                    }
                }
                log.info("Found "+matchingContractsList.size()+" Contracts for the requirement.");

                finalList.add(matchingContractsList);
            }
            log.info("All room requests processed successfully. Returning the final list of matching contracts.");
            return finalList;
        }

        else{
            throw new InvalidInputException("Search request validation failed: Number of rooms and number of adults lists must have the same length and must be NOT EMPTY.");
        }
    }

    @Scheduled(fixedDelay = EXPIRED_DATE_CHECK_DELAY)
    private void checkExpiration(){
        log.info("Checking for expired contracts.");
        int expiredContracts = 0;
        List<Contract> existingContractsList = contractRepo.findAll();

        for (Contract existingContract : existingContractsList){
            LocalDate contractEndDate = existingContract.getEndDate();
            LocalDate currentDate = LocalDate.now();

            if(contractEndDate.isBefore(currentDate) || contractEndDate.isEqual(currentDate)){
                existingContract.setExpired(true);
                contractRepo.save(existingContract);
                expiredContracts++;
            }
        }
        log.info("Found " + expiredContracts + " expired contracts.");

    }


}
