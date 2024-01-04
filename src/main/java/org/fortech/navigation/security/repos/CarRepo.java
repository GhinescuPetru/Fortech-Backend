package org.fortech.navigation.security.repos;

import org.fortech.navigation.security.models.Car;
import org.fortech.navigation.security.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepo extends JpaRepository<Car, String> {
    Optional<Car> findByVin(String vin);

    boolean existsByVin(String vin);
    List<Car> findByUser(User user);

    @Modifying
    void deleteByVin(String vin);
}
