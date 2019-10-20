package com.thoughtworks.parking_lot.repository;

import com.thoughtworks.parking_lot.core.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, Long> {
    ParkingOrder findOneByName(@Param("name") String name);
    List<ParkingOrder> findByNameContaining(String name);
    ParkingOrder findOneByPlateNumber(String plateNumber);
    ParkingOrder findOneByOrderNumber(Long orderNumber);
}
