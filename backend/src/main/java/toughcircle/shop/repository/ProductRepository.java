package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toughcircle.shop.model.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
