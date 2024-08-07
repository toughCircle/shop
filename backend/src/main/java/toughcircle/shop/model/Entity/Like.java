package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Like 좋아요 정보를 저장합니다.
 */
@Entity
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    /**
     * 좋아요를 누른 사용자입니다.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 좋아요를 누른 상품의입니다.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
