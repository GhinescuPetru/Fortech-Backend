package org.fortech.navigation.security.repos;

import org.fortech.navigation.security.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepo extends JpaRepository<Email,String> {
}
