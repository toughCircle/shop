package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toughcircle.shop.model.Entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
