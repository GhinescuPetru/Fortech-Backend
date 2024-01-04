package org.fortech.navigation.security.services;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.fortech.navigation.security.models.ITP;
import org.fortech.navigation.security.repos.ITPRepo;
import org.fortech.navigation.security.repos.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class ITPService {

    ITPRepo itpRepo;

    RoleRepo roleRepo;

    public List<ITP> getAll() {
        return itpRepo.findAll();
    }

    public ITP getITPByUsername(String username) {
        if(itpRepo.findByUsername(username).isPresent()) {
            return itpRepo.findByUsername(username).get();
        }
        return null;
    }
}
