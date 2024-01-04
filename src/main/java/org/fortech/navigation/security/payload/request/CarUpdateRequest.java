package org.fortech.navigation.security.payload.request;

import lombok.Data;

import java.time.Instant;

@Data
public class CarUpdateRequest {

    private int km;

    private Instant itpDate;

    private Instant serviceDate;

    private Instant oilChange;

    private Instant vignette;
}
