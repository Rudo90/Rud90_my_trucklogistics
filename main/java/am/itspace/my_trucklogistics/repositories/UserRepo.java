package am.itspace.my_trucklogistics.repositories;

import am.itspace.my_trucklogistics.models.DriverStatus;
import am.itspace.my_trucklogistics.models.Role;
import am.itspace.my_trucklogistics.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {


    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(Role role);
    List<User> findByDriverStatus(DriverStatus driverStatus);
    List<User> findAll();

}
