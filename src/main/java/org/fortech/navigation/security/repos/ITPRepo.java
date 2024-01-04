package org.fortech.navigation.security.repos;

import org.fortech.navigation.security.models.ITP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ITPRepo extends JpaRepository<ITP, String> {
    Optional<ITP> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
