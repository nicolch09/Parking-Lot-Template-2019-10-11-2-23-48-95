package com.thoughtworks.parking_lot.controller;

import com.thoughtworks.parking_lot.core.ParkingOrder;
import com.thoughtworks.parking_lot.service.ParkingOrderService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parkingOrder")
public class ParkingOrderController {
    @Autowired
    ParkingOrderService parkingOrderService;

    @GetMapping(value = "/all", produces = {"application/json"})
    @ResponseStatus(code = HttpStatus.OK)
    public Iterable<ParkingOrder> getAllParkingOrder(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                     @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        return parkingOrderService.getAllParkingOrder(page, pageSize);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{orderNumber}")
    public ParkingOrder getParkingOrderByOrderNumber(@PathVariable Long orderNumber) throws NotFoundException {
        return parkingOrderService.getParkingOrderByOrderNumber(orderNumber);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping(path = "/{name}")
    public ParkingOrder getParkingOrderByName(@PathVariable String name) throws NotFoundException {
        return parkingOrderService.getParkingOrderByName(name);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public List<ParkingOrder> getParkingOrderLikeName(@RequestParam(required = false) String name) throws NotFoundException {
        return parkingOrderService.getParkingLotLikeName(name);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping(path = "/{orderNumber}")
    public ParkingOrder deleteParkingOrder(@PathVariable Long orderNumber) throws NotFoundException {
        return parkingOrderService.deleteParkingOrder(orderNumber);
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PatchMapping(path = "/{orderNumber}", consumes = "application/json", produces = "application/json")
    public ParkingOrder setParkingOrderStatusAsClosed(@PathVariable Long orderNumber) throws NotFoundException {
        return parkingOrderService.setParkingOrderStatusAsClosed(orderNumber);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(path = "/{name}",produces = {"application/json"})
    public ParkingOrder saveParkingOrder(@RequestBody ParkingOrder parkingOrder,
                                         @PathVariable String name) throws NotFoundException {
        return parkingOrderService.saveParkingOrder(parkingOrder, name);
    }
}
