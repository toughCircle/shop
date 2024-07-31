package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toughcircle.shop.model.Entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_id(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);
}
