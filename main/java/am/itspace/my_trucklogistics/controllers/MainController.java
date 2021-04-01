package am.itspace.my_trucklogistics.controllers;


import am.itspace.my_trucklogistics.models.User;
import am.itspace.my_trucklogistics.service.OrderService;
import am.itspace.my_trucklogistics.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/")
    public String mainPage(ModelMap modelMap){
        return "index";
    }

    @GetMapping("/loginToAccount")
    public String login(ModelMap modelmap){
        return "login-to-account";
    }

    @GetMapping("/registerLogin")
    public String goToRegisterPage(ModelMap modelMap){
        return "RegisterLogin";
    }

    @GetMapping("/register")
    public String registration(ModelMap modelMap){
        return "regForm";
    }

}
