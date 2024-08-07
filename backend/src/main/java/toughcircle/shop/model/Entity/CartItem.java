package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * CartItem 장바구니의 상품 정보를 저장합니다.
 */
@Entity
@Getter @Setter
@Table(name = "cartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartItemId")
    private Long id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

    /**
     * 장바구니에 포함된 상품입니다.
     */
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
}
