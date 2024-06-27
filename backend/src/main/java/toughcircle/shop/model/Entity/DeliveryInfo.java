package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "deliveryInfos")
public class DeliveryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveryInfoId")
    private Long id;

    private String recipientName;
    private String phone;
    private String address;
    private String orderMemo;

    @OneToOne(mappedBy = "deliveryInfo")
    private Order order;

}
