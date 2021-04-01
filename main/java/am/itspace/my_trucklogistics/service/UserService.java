package am.itspace.my_trucklogistics.service;


import am.itspace.my_trucklogistics.models.DriverStatus;
import am.itspace.my_trucklogistics.models.Orders;
import am.itspace.my_trucklogistics.models.Role;
import am.itspace.my_trucklogistics.models.User;
import am.itspace.my_trucklogistics.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public void saveUser (User user){
        userRepo.save(user);
    }

    public List<User> findAllUsers(Role role){
       return userRepo.findAllByRole(role);
    }

    public User getOne(int id){
       return userRepo.getOne(id);
    }

    public Optional<User> findUserByEmail(String email){
        if(userRepo.findByEmail(email).isPresent()){
            return userRepo.findByEmail(email);
        }
        return Optional.empty();
    }

    public List<User> findByDriver(DriverStatus driverStatus){
        if (driverStatus == DriverStatus.FREE){
           return userRepo.findByDriverStatus(driverStatus);
        }
        else return null;
    }

    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

}
