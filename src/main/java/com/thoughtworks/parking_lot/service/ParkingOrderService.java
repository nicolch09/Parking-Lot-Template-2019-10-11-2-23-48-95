package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.core.ParkingLot;
import com.thoughtworks.parking_lot.core.ParkingOrder;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.isNull;

public class ParkingOrderService {
    private static final String PARKING_LOT_CREATION_ERROR_MESSAGE = "ERROR. Parking Lot Name doesn't exist or no more capacity available";
    private static final String OBJECT_NOT_FOUND = "OBJECT NOT FOUND";

    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @Autowired
    ParkingLotRepository parkingLotRepository;

    public Iterable<ParkingOrder> getAllParkingOrder(Integer page, Integer pageSize) {
        return parkingOrderRepository.findAll(PageRequest.of(page, pageSize));
    }

    public ParkingOrder getParkingOrderByOrderNumber(Long orderNumber) throws NotFoundException {
        ParkingOrder parkingOrder = parkingOrderRepository.findOneByOrderNumber(orderNumber);
        if (parkingOrder != null) {
            return parkingOrder;
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingOrder getParkingOrderByName(String name) throws NotFoundException {
        ParkingOrder parkingOrder = parkingOrderRepository.findOneByName(name);
        if (parkingOrder != null) {
            return parkingOrder;
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public List<ParkingOrder> getParkingLotLikeName(String name) throws NotFoundException {
        List<ParkingOrder> parkingOrder = parkingOrderRepository.findByNameContaining(name);
        if (parkingOrder.size() != 0) {
            return parkingOrderRepository.findByNameContaining(name);
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingOrder deleteParkingOrder(Long orderNumber) throws NotFoundException {
        ParkingOrder parkingOrder = parkingOrderRepository.findOneByOrderNumber(orderNumber);
        if (!isNull(parkingOrder)) {
            parkingOrderRepository.delete(parkingOrder);
            return parkingOrder;
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingOrder setParkingOrderStatusAsClosed(Long orderNumber) throws NotFoundException {
        ParkingOrder foundParkingOrder = parkingOrderRepository.findOneByOrderNumber(orderNumber);
        if (!isNull(foundParkingOrder)) {
            ParkingLot foundParkingName = parkingLotRepository.findOneByName(foundParkingOrder.getName());
            Integer parkingLotCapacity = foundParkingName.getCapacity() + 1;
            foundParkingOrder.setOrderStatus("Closed");
            foundParkingOrder.setCloseTime(getCurrentDateTime());
            foundParkingName.setCapacity(parkingLotCapacity);
            return parkingOrderRepository.save(foundParkingOrder);
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingOrder saveParkingOrder(ParkingOrder parkingOrder, String name) throws NotFoundException {
        ParkingLot foundParkingName = parkingLotRepository.findOneByName(name);
        if(!isNull(foundParkingName) && foundParkingName.getCapacity() > 0){
            return parkingOrderRepository.save(parkingOrder);
        }
        throw new NotFoundException(PARKING_LOT_CREATION_ERROR_MESSAGE);
    }

    public String getCurrentDateTime() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }
}
