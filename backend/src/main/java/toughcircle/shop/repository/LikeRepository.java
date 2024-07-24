package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toughcircle.shop.model.Entity.Like;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUser_id(Long userId);
    Optional<Like> findByUser_idAndProduct_id(Long userId, Long productId);
}
