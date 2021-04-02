package am.itspace.myTrucklogistics.controllers;

import am.itspace.myTrucklogistics.models.Enable;
import am.itspace.myTrucklogistics.models.Orders;
import am.itspace.myTrucklogistics.models.Role;
import am.itspace.myTrucklogistics.security.CurrentUser;
import am.itspace.myTrucklogistics.service.OrderService;
import am.itspace.myTrucklogistics.service.UserService;
import am.itspace.myTrucklogistics.service.emailService.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/myAccount/user/orders")
    public String ordersPage(@AuthenticationPrincipal CurrentUser principal, ModelMap modelMap) {
        if (principal != null && principal.getCustomer().getRole().equals(Role.CUSTOMER)
                && principal.getCustomer().getEnable()!=Enable.DISABLED) {
            modelMap.addAttribute("brokers", userService.findAllUsers(Role.BROKER));
            modelMap.addAttribute("customer", principal.getCustomer().getEmail());
            return "orderPage";
        }
        return "redirect:/loginToAccount";
    }

    @PostMapping("/myAccount/user/orders")
    public String makeOrder(@AuthenticationPrincipal CurrentUser principal, @ModelAttribute Orders order) {
        if (principal != null && principal.getCustomer().getEnable()!=Enable.DISABLED &&
                                principal.getCustomer().getRole().equals(Role.CUSTOMER)) {
            orderService.addOrder(order);
            return "redirect:/myAccount";
        }
        return "redirect:/loginToAccount";
    }

    @GetMapping("/myAccount/user/myOrders")
    public String myOrdersList(@AuthenticationPrincipal CurrentUser principal, ModelMap modelMap) {
        if (principal != null && principal.getCustomer().getEnable()!=Enable.DISABLED &&
                                principal.getCustomer().getRole().equals(Role.CUSTOMER)) {
            modelMap.addAttribute("myOrders", orderService.getByCustomerDetails(principal.getCustomer().getEmail()));
            modelMap.addAttribute("customer", principal.getCustomer().getEmail());
            return "myOrdersPage";
        }
        return "redirect:/loginToAccount";
    }

    @GetMapping("/myAccount/orders")
    public String brokerOrders(@AuthenticationPrincipal CurrentUser principal,
                               ModelMap modelMap) {
        if (principal != null && principal.getCustomer().getEnable()!=Enable.DISABLED &&
                principal.getCustomer().getRole().equals(Role.BROKER)) {
            modelMap.addAttribute("brokerOrders", orderService.getBrokerOrders(principal));
            return "brokerOrdersPage";
        }
        return "redirect:/";
    }

    @PostMapping("/myAccount/broker/orders")
    public String brokerOrders(@AuthenticationPrincipal CurrentUser principal,
                               @RequestParam(value = "driverDetails", required = false) int driverId,
                               @RequestParam(value = "id") int id) {

        if (principal != null && principal.getCustomer().getEnable()!=Enable.DISABLED) {
            orderService.postBrokerOrders(id, driverId, emailSenderService);
            return "redirect:/myAccount";
        }
        return "redirect:/loginToAccount";
    }
}
