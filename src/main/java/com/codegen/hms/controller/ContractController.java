package com.codegen.hms.controller;

import com.codegen.hms.dto.ContractRequestDTO;
import com.codegen.hms.dto.ContractResponseDTO;
import com.codegen.hms.dto.SearchRequestDTO;
import com.codegen.hms.entity.Contract;
import com.codegen.hms.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "api/v1/contract")
@CrossOrigin(origins = "*")
public class ContractController {

    @Autowired
    private ContractService contractService;

    static Logger log = Logger.getLogger(ContractController.class.getName());

    @GetMapping("/all")
    public ResponseEntity<List<ContractResponseDTO>> getAllContracts(){
        log.info("Initializing findAllContracts method");
        List<ContractResponseDTO> contracts = contractService.findAllContracts();
        return new ResponseEntity<>(contracts,HttpStatus.OK);
    }

    @GetMapping("/get/{page}/{size}")
    public ResponseEntity<List<ContractResponseDTO>> getPagedContracts(@PathVariable("page") int page, @PathVariable("size") int size){
        log.info("Initializing findAllContractsPageable method");
        List<ContractResponseDTO> contracts = contractService.getPagedContracts(page, size);
        return new ResponseEntity<>(contracts,HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ContractResponseDTO> getContractById(@PathVariable("id") Long id){
        log.info("Initializing getContractById method.");
        ContractResponseDTO contractResponseDTO = contractService.getContractById(id);
        return new ResponseEntity<>(contractResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ContractResponseDTO> addFullContract(@Valid @RequestBody ContractRequestDTO contractRequestDTO) {
        log.info("Initializing addFullContract method");
        ContractResponseDTO contract = contractService.addFullContact(contractRequestDTO);
        return new ResponseEntity<>(contract, HttpStatus.CREATED);

    }

    @PutMapping("/update")
    public ResponseEntity<ContractResponseDTO> updateFullContract(@Valid @RequestBody ContractRequestDTO contractRequestDTO) {
        log.info("Initializing updateFullContract method");
        ContractResponseDTO contractResponseDTO = contractService.updateFullContract(contractRequestDTO);
        return new ResponseEntity<>(contractResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFullContract(@PathVariable("id") Long id){
        log.info("Initializing deleteFullContract method");
        contractService.deleteFullContract(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<ContractResponseDTO>> searchContracts(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate, @RequestParam("noOfNights") Integer noOfNights) {
//        List<ContractResponseDTO> contractList = contractService.searchContracts(checkInDate, noOfNights);
//        return new ResponseEntity<>(contractList, HttpStatus.OK);
//    }

//    @GetMapping("/search")
//    public ResponseEntity<List<ContractResponseDTO>> searchContracts(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkInDate, @RequestParam("noOfNights") Integer noOfNights, @RequestParam("noOfAdults") Integer noOfAdults) {
//        List<ContractResponseDTO> contractList = contractService.searchAvailableContracts(checkInDate, noOfNights, noOfAdults);
//        return new ResponseEntity<>(contractList, HttpStatus.OK);
//    }

    @PostMapping("/search")
    public ResponseEntity<List<List<ContractResponseDTO>>> searchContracts(@RequestBody SearchRequestDTO searchRequestDTO) {
        log.info("Initializing searchContract method");
        List<List<ContractResponseDTO>> searchResultsList = contractService.searchContracts(searchRequestDTO);
        return new ResponseEntity<>(searchResultsList, HttpStatus.OK);
    }

}
