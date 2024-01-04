package org.fortech.navigation.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Data
@Entity
@NoArgsConstructor
@Table(name = "car_services",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phoneNumber")
        })
public class CarService {

    @Id
    @Size(max = 30)
    private String username;

    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "rolename"))
    private Set<Role> roles = new HashSet<>();

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 60)
    private String location;

    @Size(max = 20)
    private String rating;

    public CarService(String username, String email, String password, String phoneNumber, String location, String rating) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.rating = rating;
    }
}
