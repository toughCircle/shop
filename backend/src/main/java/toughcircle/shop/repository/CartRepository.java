package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toughcircle.shop.model.Entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
