package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart 장바구니 정보를 저장합니다.
 */
@Entity
@Getter @Setter
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private Long id;

    private LocalDateTime createAt;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    /**
     * 장바구니에 저장된 상품의 리스트입니다.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CartItem> cartItemList = new ArrayList<>();
}
