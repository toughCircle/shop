package toughcircle.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import toughcircle.shop.model.Entity.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.zipCode = :zipCode AND a.streetAddress = :streetAddress AND a.addressDetail = :addressDetail")
    Optional<Address> findByAddressInfo(String zipCode, String streetAddress, String addressDetail);
}
