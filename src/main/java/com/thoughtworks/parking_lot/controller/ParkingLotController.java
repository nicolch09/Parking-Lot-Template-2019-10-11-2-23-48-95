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
    public Iterable<ParkingLot> getAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                       @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        return parkingLotService.getAll(page, pageSize);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{name}")
    public ParkingLot get(@PathVariable String name) throws NotFoundException {
        return parkingLotService.get(name);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public List<ParkingLot> getSpecific(@RequestParam(required = false) String name) throws NotFoundException {
        return parkingLotService.getSpecific(name);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{name}")
    public ParkingLot delete(@PathVariable String name) throws NotFoundException {
        return parkingLotService.delete(name);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping(path = "/{name}", consumes = "application/json", produces = "application/json")
    public ParkingLot modify(@RequestBody ParkingLot parkingLot, @PathVariable String name) throws NotFoundException, NotSupportedException {
        return parkingLotService.modify(parkingLot, name);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(produces = {"application/json"})
    public ParkingLot add(@RequestBody ParkingLot parkingLot) throws NotFoundException, NotSupportedException {
        return parkingLotService.add(parkingLot);
    }
}
