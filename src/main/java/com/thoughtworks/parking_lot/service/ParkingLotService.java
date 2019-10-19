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
    private static final String ADDING_EXCEPTION = "NAME ALREADY EXISTS OR INVALID CAPACITY";
    private static final String INVALID_CAPACITY = "INVALID CAPACITY";

    public Iterable<ParkingLot> getAll(Integer page, Integer pageSize) {
        return parkingLotRepository.findAll(PageRequest.of(page, pageSize));
    }

    public ParkingLot get(String name) throws NotFoundException {
        ParkingLot parkingLot = parkingLotRepository.findOneByName(name);
        if (parkingLot != null) {
            return parkingLot;
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public List<ParkingLot> getSpecific(String name) throws NotFoundException {
        List<ParkingLot> parkingLot = parkingLotRepository.findByNameContaining(name);
        if (parkingLot.size() != 0) {
            return parkingLotRepository.findByNameContaining(name);
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingLot delete(String name) throws NotFoundException {
        ParkingLot parkingLot = parkingLotRepository.findOneByName(name);
        if (!isNull(parkingLot)) {
            parkingLotRepository.delete(parkingLot);
            return parkingLot;
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingLot modify(ParkingLot parkingLot, String name) throws NotFoundException, NotSupportedException {
        ParkingLot foundName = parkingLotRepository.findOneByName(name);

        if (!isNull(parkingLot)) {
            ParkingLot modifyParkingLot = foundName;
            if(parkingLot.getCapacity() != null && parkingLot.getCapacity() > 0)
                modifyParkingLot.setCapacity(parkingLot.getCapacity());
            else
                throw new NotSupportedException(INVALID_CAPACITY);
            if(parkingLot.getLocation() != null)
                modifyParkingLot.setLocation(parkingLot.getLocation());
            ParkingLot savedParkingLot = parkingLotRepository.save(modifyParkingLot);
            return savedParkingLot;
        }
        throw new NotFoundException(OBJECT_NOT_FOUND);
    }

    public ParkingLot add(ParkingLot parkingLot) throws NotSupportedException {
        ParkingLot foundName = parkingLotRepository.findOneByName(parkingLot.getName());
        if(isNull(foundName) && parkingLot.getCapacity() > 0){
            return parkingLotRepository.save(parkingLot);
        }
        throw new NotSupportedException(ADDING_EXCEPTION);
    }
}
