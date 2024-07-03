package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toughcircle.shop.model.Entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
