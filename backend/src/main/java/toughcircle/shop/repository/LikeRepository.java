package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toughcircle.shop.model.Entity.Like;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserId(Long userId);
    Optional<Like> findByUserIdAndProductId(Long userId, Long productId);
}
