package com.example.waguwagu.domain.type;

import java.util.Arrays;
import java.util.List;

public enum Transportation {
    CAR(1500, 5),
    MOTORBIKE(1300, 5),
    BICYCLE(800, 2.5),
    WALK(500, 1);

    public final int costPerKm;
    public final double maxDistance; // km

    Transportation(int costPerKm, double maxDistance) {
        this.costPerKm = costPerKm;
        this.maxDistance = maxDistance;
    }

    public static List<Transportation> chooseTransportationByDistance(double distanceFromStoreToCustomer) {
        List<Transportation> filteredTransportations = Arrays.stream(Transportation.values())
                .filter(transportation -> distanceFromStoreToCustomer <= transportation.maxDistance)
                .toList();

        return filteredTransportations;
    }
}
