package org.fortech.navigation.security.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.fortech.navigation.security.models.CarService;
import org.fortech.navigation.security.repos.CarServiceRepo;
import org.fortech.navigation.security.repos.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class CarServiceService {


    CarServiceRepo carServiceRepo;

    RoleRepo roleRepo;

    public List<CarService> getAll() {
        return carServiceRepo.findAll();
    }

    public CarService getCarServiceByUsername(String username) {
        if(carServiceRepo.findByUsername(username).isPresent()) {
            return carServiceRepo.findByUsername(username).get();
        }
        return null;
    }

}
