package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toughcircle.shop.model.Entity.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
}
