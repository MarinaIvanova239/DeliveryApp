package com.praktikum.delivery;

import com.praktikum.delivery.entities.Dimension;
import com.praktikum.delivery.entities.Workload;
import com.praktikum.delivery.error.IncorrectDeliveryException;

import java.util.HashMap;
import java.util.Map;

public class Calculator {
    private final static long minDeliveryPrice = 400;
    private final Map<Workload, Double> workloadCoefficient = new HashMap<Workload, Double>(){{
        put(Workload.very_high, 1.6);
        put(Workload.high, 1.4);
        put(Workload.increased, 1.2);
        put(Workload.standard, 1.0);
    }};
    private final Map<Dimension, Integer> dimensionCoefficient = new HashMap<Dimension, Integer>(){{
        put(Dimension.large, 200);
        put(Dimension.small, 100);
    }};

    public double countDelivery(int distanceInKm, Dimension size, boolean isFragile, Workload currentWorkload)
            throws IncorrectDeliveryException {
        long deliveryPrice = 0;

        // validate parameters
        if (distanceInKm <= 0) throw new IncorrectDeliveryException();
        if (isFragile && distanceInKm > 30.0) throw new IncorrectDeliveryException();

        // take fragility into account
        if (isFragile) deliveryPrice += 300;

        // take distance into account
        if (distanceInKm <= 2) deliveryPrice += 50;
        else if (distanceInKm <= 10) deliveryPrice += 100;
        else if (distanceInKm <= 30) deliveryPrice += 200;
        else deliveryPrice += 300;

        // take into account delivery size
        deliveryPrice += dimensionCoefficient.get(size);

        //  take into account the workload coefficient
        deliveryPrice *= workloadCoefficient.get(currentWorkload);

        // delivery price should be not less than minimum delivery price
        if (deliveryPrice < minDeliveryPrice) deliveryPrice = minDeliveryPrice;
        return deliveryPrice;
    }
}
