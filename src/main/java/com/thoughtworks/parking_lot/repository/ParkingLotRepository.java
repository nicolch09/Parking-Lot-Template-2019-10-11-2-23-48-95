package com.thoughtworks.parking_lot.repository;

import com.thoughtworks.parking_lot.core.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
    ParkingLot findOneByName(@Param("name") String name);
    List<ParkingLot> findByNameContaining(String name);
}
