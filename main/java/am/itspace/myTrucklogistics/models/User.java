package am.itspace.myTrucklogistics.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "name cannot be empty")
    private String name;
    @NotEmpty(message = "surname cannot be empty")
    private String surname;
    @NotEmpty(message = "username cannot be empty")
    private String username;
    @Email(message = "Input valid email")
    private String email;
    @NotEmpty(message = "specify your address")
    private String address;
    @NotEmpty(message = "specify your phone number")
    private String phone;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @NotEmpty(message = "password field cannot be empty")
    private String password;
    private String photoUrl;
    @Enumerated(value = EnumType.STRING)
    private DriverStatus driverStatus;
    @Enumerated(value = EnumType.STRING)
    private Enable enable;



}
