package am.itspace.my_trucklogistics.repositories;

import am.itspace.my_trucklogistics.models.Orders;
import am.itspace.my_trucklogistics.models.Status;
import am.itspace.my_trucklogistics.models.User;
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
