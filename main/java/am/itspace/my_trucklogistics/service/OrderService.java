package am.itspace.my_trucklogistics.service;


import am.itspace.my_trucklogistics.models.Orders;
import am.itspace.my_trucklogistics.models.Status;
import am.itspace.my_trucklogistics.models.User;
import am.itspace.my_trucklogistics.repositories.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;

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
}
