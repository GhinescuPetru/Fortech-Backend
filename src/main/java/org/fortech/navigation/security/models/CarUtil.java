package org.fortech.navigation.security.models;

import org.fortech.navigation.security.payload.response.CarResponse;

public class CarUtil {

    public static CarResponse build(Car car) {
        return new CarResponse(car.getVin(),
                car.getPlateNumber(),
                car.getBrand(),
                car.getModel(),
                car.getProductionYear(),
                car.getKm(),
                car.getItpDate(),
                car.getServiceDate(),
                car.getOilChange(),
                car.getVignette());
    }
}
