package com.example.parkmate;

public class ParkingRecord {

    public final int id;
    public String vehicleNumber;
    public String vehicleType;
    public int hours;
    public boolean paid;

    public ParkingRecord(int id, String vehicleNumber,
                         String vehicleType, int hours, boolean paid) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.hours = hours;
        this.paid = paid;
    }

    public static int getRateForType(String vehicleType) {
        if ("Motorcycle".equals(vehicleType)) {
            return 50;
        } else if ("Car".equals(vehicleType)) {
            return 100;
        } else if ("Van".equals(vehicleType)) {
            return 150;
        }

        throw new IllegalArgumentException("Unknown vehicle type");
    }

    public int getHourlyRate() {
        return getRateForType(vehicleType);
    }

    public int getTotalFee() {
        return getHourlyRate() * hours;
    }
}