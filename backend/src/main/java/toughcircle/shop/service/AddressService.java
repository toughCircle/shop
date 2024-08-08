package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toughcircle.shop.model.Entity.Address;
import toughcircle.shop.model.Entity.User;
import toughcircle.shop.model.dto.AddressDto;
import toughcircle.shop.repository.AddressRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    /**
     * 주소 저장
     * @param user 사용자 정보
     * @param addressRequest 주소 정보
     */
    @Transactional
    public void saveAddressInfo(User user, AddressDto addressRequest) {
        Optional<Address> existingAddress = addressRepository.findByAddressInfo(
            addressRequest.getZipCode(),
            addressRequest.getStreetAddress(),
            addressRequest.getAddressDetail()
        );

        Address address = existingAddress.orElseGet(() -> {
            Address newAddress = new Address();
            newAddress.setZipCode(addressRequest.getZipCode());
            newAddress.setStreetAddress(addressRequest.getStreetAddress());
            newAddress.setAddressDetail(addressRequest.getAddressDetail());
            newAddress.setAddressType(addressRequest.getAddressType());
            newAddress.setUser(user);
            addressRepository.save(newAddress);
            return newAddress;
        });

        if (user.getAddressList() == null) {
            user.setAddressList(new ArrayList<>());
        }
        if (!user.getAddressList().contains(address)) {
            user.getAddressList().add(address);
        }
    }
}
