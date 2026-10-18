package com.example.parkmate;

import java.util.ArrayList;

public class ParkingStore {

    private static final ArrayList<ParkingRecord> records = new ArrayList<>();
    private static int nextId = 1;

    public static ParkingRecord add(String vehicleNumber, String vehicleType,
                                    int hours, boolean paid) {
        ParkingRecord record = new ParkingRecord(
                nextId, vehicleNumber, vehicleType, hours, paid);

        records.add(record);
        nextId++;
        return record;
    }

    public static ArrayList<ParkingRecord> getAll() {
        return new ArrayList<>(records);
    }

    public static ParkingRecord findById(int id) {
        for (ParkingRecord record : records) {
            if (record.id == id) {
                return record;
            }
        }

        return null;
    }

    public static boolean update(int id, String vehicleNumber,
                                 String vehicleType, int hours, boolean paid) {
        ParkingRecord record = findById(id);

        if (record == null) {
            return false;
        }

        record.vehicleNumber = vehicleNumber;
        record.vehicleType = vehicleType;
        record.hours = hours;
        record.paid = paid;
        return true;
    }

    public static boolean delete(int id) {
        ParkingRecord record = findById(id);

        if (record == null) {
            return false;
        }

        return records.remove(record);
    }

    public static int getCount() {
        return records.size();
    }

    public static int getCollectedTotal() {
        int total = 0;

        for (ParkingRecord record : records) {
            if (record.paid) {
                total += record.getTotalFee();
            }
        }

        return total;
    }
}