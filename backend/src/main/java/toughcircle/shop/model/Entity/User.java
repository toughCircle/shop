package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * User 사용자 정보를 저장합니다.
 */
@Entity
@Getter @Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    private String username;
    private String email;
    private String password;
    private String phone;

    /**
     * 사용자가 저장한 주소 리스트입니다.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Address> addressList = new ArrayList<>();

    /**
     * 사용자가 작성한 리뷰 리스트입니다.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    /**
     * 사용자의 장바구니입니다.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Cart cart;

    /**
     * 사용자의 주문 내역입니다.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Order> orderList = new ArrayList<>();
}
