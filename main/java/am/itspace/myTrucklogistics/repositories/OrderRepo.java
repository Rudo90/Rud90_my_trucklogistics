package am.itspace.myTrucklogistics.repositories;

import am.itspace.myTrucklogistics.models.Orders;
import am.itspace.myTrucklogistics.models.Status;
import am.itspace.myTrucklogistics.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepo extends JpaRepository<Orders, Integer> {

    List<Orders> getOrdersByUser(User user);
    List<Orders> getOrdersByCustomerDetails(String details);
    List<Orders> getOrdersByStatus(Status status);
    List<Orders> getOrdersByDriverDetails(String details);
    Orders getOrdersById(int id);
    List<Orders> findAll();

}
