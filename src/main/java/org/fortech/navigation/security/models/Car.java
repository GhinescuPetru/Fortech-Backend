package org.fortech.navigation.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "plateNumber")
        })
public class Car {

    @Id
    @Size(max = 20)
    private String vin;

    @Size(max = 30)
    private String plateNumber;

    @Size(max = 50)
    private String brand;

    @Size(max = 50)
    private String model;

    private int productionYear;

    private int km;

    private Instant itpDate;

    private Instant serviceDate;

    private Instant oilChange;

    private Instant vignette;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;


    public Car(String vin, String plateNumber, String brand, String model, int productionYear, int km, Instant itpDate, Instant serviceDate, Instant oilChange, Instant vignette) {
        this.vin = vin;
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.model = model;
        this.productionYear = productionYear;
        this.km = km;
        this.itpDate = itpDate;
        this.serviceDate = serviceDate;
        this.oilChange = oilChange;
        this.vignette = vignette;
    }
}
