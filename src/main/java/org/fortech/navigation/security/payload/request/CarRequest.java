package org.fortech.navigation.security.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
public class CarRequest {


    @NotBlank
    private String vin;

    @NotBlank
    private String plateNumber;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @NotBlank
    private int productionYear;

    private int km;

    private Instant itpDate;

    private Instant serviceDate;

    private Instant oilChange;

    private Instant vignette;
}
