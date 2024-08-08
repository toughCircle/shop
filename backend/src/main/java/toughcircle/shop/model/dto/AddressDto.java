package toughcircle.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toughcircle.shop.model.Enums.AddressType;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    @JsonProperty("address_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long Id;

    /**
     * 주소 별칭 [ HOME(집) / WORK(회사) ]
     */
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private String zipCode;
    private String streetAddress;
    private String addressDetail;
}
