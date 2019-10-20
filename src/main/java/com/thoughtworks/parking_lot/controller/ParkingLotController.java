package com.thoughtworks.parking_lot.controller;

import com.thoughtworks.parking_lot.core.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.NotSupportedException;
import java.util.List;

@RestController
@RequestMapping("/parkingLot")
public class ParkingLotController {
    @Autowired
    private ParkingLotService parkingLotService;

    @GetMapping(value = "/all", produces = {"application/json"})
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<ParkingLot> getAllParkingLot(@RequestParam(required = false, defaultValue = "0") Integer page,
                                       @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        return parkingLotService.getAllParkingLot(page, pageSize);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{name}")
    public ParkingLot getParkingLotByName(@PathVariable String name) throws NotFoundException {
        return parkingLotService.getParkingLotByName(name);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public List<ParkingLot> getParkingLotLikeName(@RequestParam(required = false) String name) throws NotFoundException {
        return parkingLotService.getParkingLotLikeName(name);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{name}")
    public ParkingLot deleteParkingLot(@PathVariable String name) throws NotFoundException {
        return parkingLotService.deleteParkingLot(name);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping(path = "/{name}", consumes = "application/json", produces = "application/json")
    public ParkingLot modifyParkingLot(@RequestBody ParkingLot parkingLot, @PathVariable String name) throws NotFoundException, NotSupportedException {
        return parkingLotService.modifyParkingLot(parkingLot, name);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(produces = {"application/json"})
    public ParkingLot saveParkingLot(@RequestBody ParkingLot parkingLot) throws NotSupportedException {
        return parkingLotService.saveParkingLot(parkingLot);
    }
}
