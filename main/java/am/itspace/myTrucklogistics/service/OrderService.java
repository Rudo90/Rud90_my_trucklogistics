package am.itspace.myTrucklogistics.service;


import am.itspace.myTrucklogistics.models.DriverStatus;
import am.itspace.myTrucklogistics.models.Orders;
import am.itspace.myTrucklogistics.models.Status;
import am.itspace.myTrucklogistics.models.User;
import am.itspace.myTrucklogistics.repositories.OrderRepo;
import am.itspace.myTrucklogistics.repositories.UserRepo;
import am.itspace.myTrucklogistics.security.CurrentUser;
import am.itspace.myTrucklogistics.service.emailService.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;

    public List<Orders> getOrdersByUser(User user){
        return orderRepo.getOrdersByUser(user);
    }

    public Orders getById(int id){
        return orderRepo.getOrdersById(id);
    }

    public void addOrder(Orders order){
        orderRepo.save(order);
    }

    public List<Orders> getByCustomerDetails(String details){
       return orderRepo.getOrdersByCustomerDetails(details);
    }

    public List<Orders> getOrdersByStatus(Status status){
        return orderRepo.getOrdersByStatus(status);
    }

    public List<Orders> getByDriver(String details){
        return orderRepo.getOrdersByDriverDetails(details);
    }

    public List<Orders> getAllOrders(){
        return orderRepo.findAll();
    }

    public void postBrokerOrders(int id, int driverId, EmailSenderService emailSenderService){

        Orders orders = orderRepo.getOrdersById(id);
        User driver = userRepo.getOne(driverId);
        orders.setDriverDetails(driver.getEmail());
        orders.setStatus(Status.ON_WAY);
        driver.setDriverStatus(DriverStatus.BUSY);
        orderRepo.save(orders);
        Optional<User> userByEmail = userRepo.findByEmail(orders.getCustomerDetails());
        userRepo.save(driver);
        if (userByEmail.isPresent()) {
            User customer = userByEmail.get();
            emailSenderService.sandSimpleMessage("email_name", orders.getCustomerDetails(),
                    "Verification success", "Dear " + customer.getName() + " " + customer.getSurname() +
                            " welcome to your account");
        }
    }

    public List<Orders> getBrokerOrders(CurrentUser principal){

        List<Orders> orderList = orderRepo.getOrdersByUser(principal.getCustomer());
        for (Orders orders : orderList) {
            String details = orders.getDriverDetails();
            User driver = userRepo.findByEmail(details).get();
            if (driver.getDriverStatus().equals(DriverStatus.FREE)){
                orders.setStatus(Status.DELIVERED);
                orderRepo.save(orders);
            }
        }
        return orderList;

    }

}
