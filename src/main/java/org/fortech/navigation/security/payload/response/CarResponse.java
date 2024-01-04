package org.fortech.navigation.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CarResponse {

    private String vin;

    private String plateNumber;

    private String brand;

    private String model;

    private int productionYear;

    private int km;

    private Instant itpDate;

    private Instant serviceDate;

    private Instant oilChange;

    private Instant vignette;
}
