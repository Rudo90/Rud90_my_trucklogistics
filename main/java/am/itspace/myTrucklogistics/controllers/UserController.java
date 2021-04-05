package am.itspace.myTrucklogistics.controllers;

import am.itspace.myTrucklogistics.models.*;
import am.itspace.myTrucklogistics.security.CurrentUser;
import am.itspace.myTrucklogistics.service.OrderService;
import am.itspace.myTrucklogistics.service.UserService;
import am.itspace.myTrucklogistics.service.emailService.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;

    @Autowired
    EmailSenderService emailSenderService;

    @Value("${user.upload.dir}")
    private String uploadDir;

    @PostMapping ("/register")
    public String registerCustomer(@ModelAttribute @Valid User user, BindingResult bindingResult,
                                   ModelMap modelMap, @RequestParam("image")MultipartFile image) {

        if (bindingResult.hasErrors()){
            return "regForm";
        }

        if (userService.findUserByEmail(user.getEmail()).isPresent()){
            return "redirect:/register?message=User already exist";
        }
        if (image != null && !image.isEmpty()) {
            userService.saveUserWithImage(user, image, passwordEncoder, emailSenderService);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRole().equals(Role.DRIVER)){
            user.setDriverStatus(DriverStatus.FREE);
        }
        userService.saveUser(user);
        emailSenderService.sandSimpleMessage("email_name", user.getEmail(),
                "Verification success", "Dear " + user.getName() + " " + user.getSurname() +
                        " welcome to your account");
        return "redirect:/loginToAccount";

    }

    @PostMapping ("/myAccount/user/update")
    public String updateCustomer(@ModelAttribute User user,
                                 @RequestParam("status") Enable enable,
                                 @RequestParam("image")MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String photoUrl = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            File file = new File(uploadDir + File.separator + photoUrl);
            image.transferTo(file);
            user.setPhotoUrl(photoUrl);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnable(enable);
        }
        userService.saveUser(user);
        return "userAccount";
    }


    @GetMapping("/myAccount")
    public String mainPage(@AuthenticationPrincipal CurrentUser principal, ModelMap model) {
        if (principal != null && principal.getCustomer().getEnable()!=Enable.DISABLED){
            if (principal.getCustomer().getRole().equals(Role.CUSTOMER)){
                return "userAccount";
            }
            if (principal.getCustomer().getRole().equals(Role.BROKER)) {
                userService.getMyAccount(principal, model);
                model.addAttribute("freeDrivers", userService.findByDriver(DriverStatus.FREE));
                return "Broker-homepage";
            }
            if (principal.getCustomer().getRole().equals(Role.DRIVER)) {
                List<Orders> list = orderService.getByDriver(principal.getCustomer().getEmail());
                model.addAttribute("driverOrders", list);
                if (!list.isEmpty()){
                    principal.getCustomer().setDriverStatus(DriverStatus.BUSY);
                    userService.saveUser(principal.getCustomer());
                }
                return "DriverHomepage";
            }
            if (principal.getCustomer().getRole().equals(Role.ADMIN)){
                List<Orders> list = orderService.getAllOrders();
                List<User> userList = userService.getAllUsers();
                model.addAttribute("allOrders", list);
                model.addAttribute("allUsers", userList);
                return "Admin";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/myAccount/user/edit")
    public String editAccount(@AuthenticationPrincipal CurrentUser principal, ModelMap modelMap){
        if(principal != null && principal.getCustomer().getEnable()!=Enable.DISABLED){
            modelMap.addAttribute("user", userService.getOne(principal.getCustomer().getId()));
            return "Edit-Profile";
        }
        return "redirect:/";
    }

    @GetMapping("/myAccount/allDrivers")
    public String allDrivers(@AuthenticationPrincipal CurrentUser principal, ModelMap modelMap){

        if (principal != null && principal.getCustomer().getRole().equals(Role.BROKER) &&
                principal.getCustomer().getEnable()!=Enable.DISABLED){
            List<User> list = userService.findAllUsers(Role.DRIVER);
            for (User user : list) {
                List<User> driverList = new ArrayList<>();
                if (user.getDriverStatus().equals(DriverStatus.BUSY)){
                    driverList.add(user);
                }
                modelMap.addAttribute("allDrivers", driverList);
            }

            return "Broker-allDrivers";
        }
        return "redirect:/loginToAccount";
    }

    @PostMapping("/myAccount/allDrivers")
    public String allDrivers(@AuthenticationPrincipal CurrentUser principal, @ModelAttribute User user,
                             @RequestParam("status") String status,
                             @RequestParam("id") int id){

        User driver = userService.getOne(id);

        if (principal != null && driver.getRole().equals(Role.DRIVER) &&
                driver.getDriverStatus().equals(DriverStatus.BUSY) && principal.getCustomer().getEnable()!=Enable.DISABLED){
            driver.setDriverStatus(DriverStatus.FREE);
            userService.saveUser(driver);
            return "Broker-homepage";
        }
        return "redirect:/loginToAccount";
    }

    @PostMapping("/myAccount/admin/editUser/")
    public String editingUser(@RequestParam(value = "id", required = false) Integer id,
                              @RequestParam("status") Enable status){

            User user = userService.getOne(id);
            user.setEnable(status);
            userService.saveUser(user);
            return "redirect:/myAccount";
    }

    @GetMapping("/myAccount/admin/editUser/")
    public String editUser(@RequestParam(value = "id", required = false) Integer id, ModelMap modelMap){

        if(id != null){
            modelMap.addAttribute("user", userService.getOne(id));
            return "editUserByAdmin";
        }
        return "Admin";
    }
}
