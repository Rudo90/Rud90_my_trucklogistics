package am.itspace.myTrucklogistics.repositories;

import am.itspace.myTrucklogistics.models.DriverStatus;
import am.itspace.myTrucklogistics.models.Role;
import am.itspace.myTrucklogistics.models.User;
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
