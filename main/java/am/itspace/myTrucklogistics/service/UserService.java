package am.itspace.myTrucklogistics.service;


import am.itspace.myTrucklogistics.models.*;
import am.itspace.myTrucklogistics.repositories.OrderRepo;
import am.itspace.myTrucklogistics.repositories.UserRepo;
import am.itspace.myTrucklogistics.security.CurrentUser;
import am.itspace.myTrucklogistics.service.emailService.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${user.upload.dir}")
    private String uploadDir;

    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

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

    public void bindingResult(User user, BindingResult bindingResult, ModelMap modelMap){

        for (ObjectError error : bindingResult.getFieldErrors()) {
            if (user.getName().length() ==0 && error.getDefaultMessage().contains("name")) {
                String msg1 = error.getDefaultMessage();
                modelMap.addAttribute("nameErr", msg1);
            }
            if (user.getSurname().length() ==0 && error.getDefaultMessage().contains("surname")) {
                String msg2 = error.getDefaultMessage();
                modelMap.addAttribute("surnameErr", msg2);
            }
            if (user.getUsername().length() ==0 && error.getDefaultMessage().contains("username")) {
                String msg3 = error.getDefaultMessage();
                modelMap.addAttribute("usernameErr", msg3);
            }
            if (user.getEmail().length()==0 && error.getDefaultMessage().contains("email")) {
                String msg4 = error.getDefaultMessage();
                modelMap.addAttribute("emailErr", msg4);
            }
            if (user.getAddress().length()==0 && error.getDefaultMessage().contains("address")) {
                String msg5 = error.getDefaultMessage();
                modelMap.addAttribute("addressErr", msg5);
            }
            if (user.getPhone().length()==0 && error.getDefaultMessage().contains("phone")) {
                String msg6 = error.getDefaultMessage();
                modelMap.addAttribute("phoneErr", msg6);
            }
            if (user.getPassword().length()==0 && error.getDefaultMessage().contains("password")) {
                String msg7 = error.getDefaultMessage();
                modelMap.addAttribute("passwordErr", msg7);
            }
        }



    }

    public void saveUserWithImage(User user, MultipartFile image, PasswordEncoder passwordEncoder,
                                  EmailSenderService emailSenderService){

        String photoUrl = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        File file = new File(uploadDir + File.separator + photoUrl);
        try {
            image.transferTo(file);
            user.setPhotoUrl(photoUrl);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
            emailSenderService.sandRegistrationAttachedMessage("email_name", user.getEmail(),
                    "Verification success", "Dear " + user.getName() + " " + user.getSurname() +
                            " welcome to your account", uploadDir + File.separator + photoUrl);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    public void getMyAccount(CurrentUser principal, ModelMap model){

        for (Orders orders : orderRepo.getOrdersByUser(principal.getCustomer())) {
            if (userRepo.findByEmail(orders.getCustomerDetails()).isPresent()) {
                User user = userRepo.findByEmail(orders.getCustomerDetails()).get();
                model.addAttribute("customerDetails", user);
            }
            if (orders.getStatus().equals(Status.PROCESSING)){
                model.addAttribute("brokersOrders", orderRepo.getOrdersByStatus(Status.PROCESSING));
            }
        }
    }

}
