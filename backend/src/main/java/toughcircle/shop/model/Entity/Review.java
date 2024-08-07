package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Review 리뷰 정보를 저장합니다.
 */
@Entity
@Getter @Setter
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId")
    private Long id;

    /**
     * 상품의 별점을 나타냅니다.
     */
    @NotNull
    private double rating;
    @NotNull
    private String comment;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String image;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
}
