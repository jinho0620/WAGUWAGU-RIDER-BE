package com.example.waguwagu.domain.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RiderTransportation {
    CAR(1500, 5),
    MOTORBIKE(1300, 5),
    BICYCLE(800, 2.5),
    WALK(500, 1);

    public final int costPerKm;
    public final double maxDistance; // km

    private RiderTransportation(int costPerKm, double maxDistance) {
        this.costPerKm = costPerKm;
        this.maxDistance = maxDistance;
    }

    public static List<RiderTransportation> chooseTransportationByDistance(double distanceFromStoreToCustomer) {
        List<RiderTransportation> filteredTransportations = Arrays.stream(RiderTransportation.values())
                .filter(transportation -> distanceFromStoreToCustomer <= transportation.maxDistance)
                .toList();

        return filteredTransportations;
    }


    public static int calculateDeliveryFeeByTransportation(RiderTransportation riderTransportation, double distanceFromStoreToCustomer) {
        int costByDistance;
        if (riderTransportation.equals(RiderTransportation.CAR)) costByDistance
                = (int) (distanceFromStoreToCustomer * RiderTransportation.CAR.costPerKm);
        else if (riderTransportation.equals(RiderTransportation.MOTORBIKE)) costByDistance
                = (int) (distanceFromStoreToCustomer * RiderTransportation.MOTORBIKE.costPerKm);
        else if (riderTransportation.equals(RiderTransportation.BICYCLE)) costByDistance
                = (int) (distanceFromStoreToCustomer * RiderTransportation.BICYCLE.costPerKm);
        else costByDistance = (int) (distanceFromStoreToCustomer * RiderTransportation.WALK.costPerKm);
        return costByDistance;
    }
}
