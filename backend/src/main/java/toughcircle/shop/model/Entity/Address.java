package toughcircle.shop.model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import toughcircle.shop.model.Enums.AddressType;

/**
 * Address 주소를 저장합니다.
 */
@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressId")
    private Long Id;

    /**
     * 주소 별칭 [ HOME(집) / WORK(회사) ]
     */
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private String zipCode;
    private String streetAddress;
    private String addressDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
}
