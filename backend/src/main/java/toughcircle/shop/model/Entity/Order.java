package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import toughcircle.shop.model.Enums.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order 주문 정보를 저장합니다.
 */
@Entity
@Getter @Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long id;

    private int totalPrice;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private String orderNumber;

    /**
     * 회원 주문을 위한 회원 정보입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    /**
     * 주문 상품의 리스트입니다.
     */
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    /**
     * 비회원 주문을 위한 정보입니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestId")
    private Guest guest;

    /**
     * 주문의 배송 정보입니다.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deliveryInfoId")
    private DeliveryInfo deliveryInfo;
}
