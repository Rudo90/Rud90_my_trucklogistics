package am.itspace.myTrucklogistics.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(value = EnumType.STRING)
    private Type type;
    @ManyToOne
    private User user;
    private String customerDetails;
    private String driverDetails;
    private String startPoint;
    private String destination;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private String orderDate;


}
