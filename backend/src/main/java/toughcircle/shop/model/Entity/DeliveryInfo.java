package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * DeliveryInfo 배송 정보를 저장합니다.
 */
@Entity
@Getter @Setter
@Table(name = "deliveryInfos")
public class DeliveryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveryInfoId")
    private Long id;

    /**
     * 수신자의 이름을 나타냅니다.
     */
    private String recipientName;

    private String phone;

    @OneToOne
    @JoinColumn(name = "addressId")
    private Address address;

    /**
     * 주문 메모를 나타냅니다.
     */
    private String orderMemo;

    /**
     * 주문 정보입니다.
     */
    @OneToOne(mappedBy = "deliveryInfo", optional = false, cascade = CascadeType.REMOVE)  // 주문과의 관계 설정
    private Order order;

}
