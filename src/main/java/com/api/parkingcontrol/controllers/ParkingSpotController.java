package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotModel parkingSpotModel){

        if(parkingSpotService.existByLicensePlateCar(parkingSpotModel.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: A placa do carro está em uso!");
        }
        if(parkingSpotService.existByParkingSpotNumber(parkingSpotModel.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: O numero da vaga está em uso!");
        }
        if(parkingSpotService.existByAparmentAndBlock(parkingSpotModel.getApartment(), parkingSpotModel.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: O numero da vaga  já está registrado para esse apartamento/bloco!");
        }
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));

    }



}
