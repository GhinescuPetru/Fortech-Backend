package org.fortech.navigation.security.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.fortech.navigation.security.exception.CarNotFoundException;
import org.fortech.navigation.security.models.Car;
import org.fortech.navigation.security.models.CarUtil;
import org.fortech.navigation.security.payload.request.CarRequest;
import org.fortech.navigation.security.payload.request.CarUpdateRequest;
import org.fortech.navigation.security.payload.response.CarResponse;
import org.fortech.navigation.security.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/cars") //Suppress warnings for mapping annotation
@SecurityRequirement(name = "bearerAuth")
public class CarController {

    @Autowired
    CarService carService;

    @GetMapping()
    public ResponseEntity<?> getCars() {
        try {
            List<Car> cars = carService.getUserCars();
            List<CarResponse> carResponse = cars.stream().map(CarUtil::build).toList();
            return ResponseEntity.ok(carResponse);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{vin}")
    public ResponseEntity<?> getCar(@PathVariable String vin) {
        try {
            Car car = carService.getCarByVin(vin);
            CarResponse carResponse = CarUtil.build(car);

            return ResponseEntity.ok(carResponse);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addCar(@RequestBody CarRequest carRequest) {
        try {
            carService.addCar(carRequest);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Car added successfully!");
    }

    @PutMapping("{vin}")
    public ResponseEntity<?> updateCar(@PathVariable String vin, @RequestBody CarUpdateRequest carUpdateRequest) {
        try {
            carService.updateCar(vin, carUpdateRequest);
        } catch (CarNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Car updated successfully!");
    }
    @DeleteMapping("{vin}")
    public ResponseEntity<?> deleteCar(@PathVariable String vin) {
        try {
            carService.deleteCar(vin);
        } catch (CarNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Car deleted successfully!");
    }


}