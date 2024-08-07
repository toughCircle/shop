package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Category 카테고리 정보를 저장합니다.
 */
@Entity
@Getter @Setter
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Long id;

    /**
     * 카테고리 이름을 나타냅니다.
     */
    private String name;

    /**
     * 카테고리의 상품 리스트입니다.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> productList = new ArrayList<>();

}
