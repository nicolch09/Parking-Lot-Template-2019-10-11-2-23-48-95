package com.thoughtworks.parking_lot.core;

import javax.persistence.*;

@Entity
public class ParkingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderNumber;
    private String name;
    @Column(unique = true)
    private String plateNumber;
    private String creationTime;
    private String closeTime;
    private String orderStatus;

    public ParkingOrder() {
    }

    public ParkingOrder(Long orderNumber, String name, String plateNumber, String creationTime, String closeTime, String orderStatus) {
        this.orderNumber = orderNumber;
        this.name = name;
        this.plateNumber = plateNumber;
        this.creationTime = creationTime;
        this.closeTime = closeTime;
        this.orderStatus = orderStatus;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
