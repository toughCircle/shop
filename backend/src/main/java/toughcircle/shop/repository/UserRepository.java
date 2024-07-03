package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toughcircle.shop.model.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
