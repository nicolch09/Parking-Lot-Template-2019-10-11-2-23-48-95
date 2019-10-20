package com.thoughtworks.parking_lot.service;

import com.thoughtworks.parking_lot.core.ParkingLot;
import com.thoughtworks.parking_lot.repository.ParkingLotRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ParkingLotService {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    private static final String OBJECT_NOT_FOUND = "OBJECT NOT FOUND";
    private static final String INVALID_CAPACITY = "INVALID CAPACITY";
    private static final String PARKING_NAME_ALREADY_EXISTS = "PARKING NAME ALREADY EXISTS";

    public Iterable<ParkingLot> getAllParkingLot(Integer page, Integer pageSize) {
        return parkingLotRepository.findAll(PageRequest.of(page, pageSize));
    }

    public ParkingLot getParkingLotByName(String name) throws NotFoundException {
        ParkingLot parkingLot = parkingLotRepository.findOneByName(name);
        if (parkingLot != null) {
            return parkingLot;
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public List<ParkingLot> getParkingLotLikeName(String name) throws NotFoundException {
        List<ParkingLot> parkingLot = parkingLotRepository.findByNameContaining(name);
        if (parkingLot.size() != 0) {
            return parkingLotRepository.findByNameContaining(name);
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingLot deleteParkingLot(String name) throws NotFoundException {
        ParkingLot parkingLot = parkingLotRepository.findOneByName(name);
        if (!isNull(parkingLot)) {
            parkingLotRepository.delete(parkingLot);
            return parkingLot;
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingLot modifyParkingLot(ParkingLot parkingLot, String name) throws NotFoundException, NotSupportedException {
        ParkingLot foundName = parkingLotRepository.findOneByName(name);

        if (!isNull(parkingLot)) {
            if(parkingLot.getCapacity() != null && parkingLot.getCapacity() > 0)
                foundName.setCapacity(parkingLot.getCapacity());
            else
                throw new NotSupportedException(INVALID_CAPACITY);
            if(parkingLot.getLocation() != null)
                foundName.setLocation(parkingLot.getLocation());
            return parkingLotRepository.save(foundName);
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingLot saveParkingLot(ParkingLot parkingLot) throws NotSupportedException {
        ParkingLot foundName = parkingLotRepository.findOneByName(parkingLot.getName());
        if(isNull(foundName)) {
            if(parkingLot.getCapacity() > 0) {
                return parkingLotRepository.save(parkingLot);
            }
            throw new NotSupportedException(INVALID_CAPACITY);
        }
        throw new NotSupportedException(PARKING_NAME_ALREADY_EXISTS);
    }
}
