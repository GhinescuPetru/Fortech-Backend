package org.fortech.navigation.security.services;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.fortech.navigation.security.exception.CarNotFoundException;
import org.fortech.navigation.security.models.Car;
import org.fortech.navigation.security.models.User;
import org.fortech.navigation.security.models.UserDetailsImpl;
import org.fortech.navigation.security.payload.request.CarRequest;
import org.fortech.navigation.security.payload.request.CarUpdateRequest;
import org.fortech.navigation.security.repos.CarRepo;
import org.fortech.navigation.security.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Log4j2
@Service
public class CarService {
    @Autowired
    CarRepo carRepo;

    @Autowired
    UserRepo userRepo;

    public List<Car> getAll() {
        return carRepo.findAll();
    }

    public Car getCarByVin(String vin) {
        if(carRepo.findByVin(vin).isPresent()) {
            return carRepo.findByVin(vin).get();
        }
        return null;
    }

    public List<Car> getUserCars() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return user.getCars();
    }

    public void addCar(CarRequest carRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        Car car = new Car(carRequest.getVin(),
                carRequest.getPlateNumber(),
                carRequest.getBrand(),
                carRequest.getModel(),
                carRequest.getProductionYear(),
                carRequest.getKm(),
                carRequest.getItpDate(),
                carRequest.getServiceDate(),
                carRequest.getOilChange(),
                carRequest.getVignette());

        car.setUser(user);
        carRepo.save(car);
    }

    public void updateCar(String vin, CarUpdateRequest carUpdateRequest) {
        Car car = carRepo.findByVin(vin)
                .orElseThrow(() -> new CarNotFoundException(vin, String.format("Car with %s not found", vin)));

        int km = carUpdateRequest.getKm();
        Instant serviceDate = carUpdateRequest.getServiceDate();
        Instant vignette = carUpdateRequest.getVignette();
        Instant itpDate = carUpdateRequest.getItpDate();
        Instant oilChange = carUpdateRequest.getOilChange();

        car.setKm(km);
        if(serviceDate != null)
            car.setServiceDate(serviceDate);
        if(itpDate != null)
            car.setItpDate(itpDate);
        if(oilChange != null)
            car.setOilChange(oilChange);
        if(vignette != null)
            car.setVignette(vignette);

        carRepo.save(car);
    }

    @Transactional
    public void deleteCar(String vin) {
        try {
            carRepo.deleteByVin(vin);
        } catch (CarNotFoundException exception) {
            log.error("Car not found");
        }
    }

}
