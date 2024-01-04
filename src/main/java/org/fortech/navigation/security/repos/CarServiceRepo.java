package org.fortech.navigation.security.repos;

import org.fortech.navigation.security.models.CarService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarServiceRepo extends JpaRepository<CarService, String> {
    Optional<CarService> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
