package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.core.ParkingLot;
import com.thoughtworks.parking_lot.core.ParkingOrder;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import com.thoughtworks.parking_lot.repository.ParkingOrderRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ParkingOrderService {
    private static final String PARKING_LOT_DOES_NOT_EXIST = "PARKING LOT DOES NOT EXIST";
    private static final String THE_PARKING_LOT_IS_FULL = "THE PARKING LOT IS FULL";
    private static final String OBJECT_NOT_FOUND = "OBJECT NOT FOUND";
    private static final String PLATE_NUMBER_EXISTS = "PLATE NUMBER EXISTS";
    private static final String ORDER_NUMBER_ALREADY_CLOSED = "ORDER NUMBER ALREADY CLOSED";

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
            if(foundParkingOrder.getOrderStatus() != null) {
                ParkingLot foundParkingName = parkingLotRepository.findOneByName(foundParkingOrder.getName());
                Integer parkingLotCapacity = foundParkingName.getCapacity() + 1;
                foundParkingOrder.setOrderStatus("Closed");
                foundParkingOrder.setCloseTime(getCurrentDateTime());
                foundParkingName.setCapacity(parkingLotCapacity);
                parkingLotRepository.save(foundParkingName);
                return parkingOrderRepository.save(foundParkingOrder);
            }
            throw new NotFoundException(ORDER_NUMBER_ALREADY_CLOSED);
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingOrder saveParkingOrder(ParkingOrder parkingOrder, String name) throws NotFoundException {
        ParkingLot foundParkingName = parkingLotRepository.findOneByName(name);
        if(!isNull(foundParkingName)){
            if(foundParkingName.getCapacity() > 0) {
                ParkingOrder foundPlateNumber = parkingOrderRepository.findOneByPlateNumber(parkingOrder.getPlateNumber());
                if(isNull(foundPlateNumber)) {
                    Integer parkingLotCapacity = foundParkingName.getCapacity() - 1;
                    foundParkingName.setCapacity(parkingLotCapacity);
                    parkingOrder.setName(name);
                    parkingOrder.setCreationTime(getCurrentDateTime());
                    parkingOrder.setOrderStatus("Open");
                    parkingLotRepository.save(foundParkingName);
                    return parkingOrderRepository.save(parkingOrder);
                }
                throw new NotFoundException(PLATE_NUMBER_EXISTS);
            }
            throw new NotFoundException(THE_PARKING_LOT_IS_FULL);
        }
        throw new NotFoundException(PARKING_LOT_DOES_NOT_EXIST);
    }

    public String getCurrentDateTime() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return myDateObj.format(myFormatObj);
    }
}
