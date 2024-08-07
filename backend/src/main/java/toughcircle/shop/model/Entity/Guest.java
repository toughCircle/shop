package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Guest 비회원 주문 정보를 저장합니다.
 */
@Entity
@Getter @Setter
@Table(name = "Guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestId")
    private Long id;

    private String name;
    private String phone;
    private String email;

    /**
     * 비회원의 주문 리스트입니다.
     */
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> orderList = new ArrayList<>();

}
