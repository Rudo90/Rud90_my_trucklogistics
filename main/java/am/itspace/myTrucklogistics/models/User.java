package am.itspace.myTrucklogistics.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "name cannot be empty")
    private String name;
    @NotBlank(message = "surname cannot be empty")
    private String surname;
    @NotBlank(message = "username cannot be empty")
    private String username;
    @Email(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$", message = "Input valid email")
    private String email;
    @NotBlank(message = "specify your address")
    private String address;
    @NotBlank(message = "specify your phone number")
    private String phone;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Size(min = 2, message = "password field cannot be empty")
    private String password;
    private String photoUrl;
    @Enumerated(value = EnumType.STRING)
    private DriverStatus driverStatus;
    @Enumerated(value = EnumType.STRING)
    private Enable enable;



}
