package toughcircle.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toughcircle.shop.model.Entity.Address;
import toughcircle.shop.model.Entity.DeliveryInfo;
import toughcircle.shop.model.Entity.Order;
import toughcircle.shop.model.dto.DeliveryInfoDto;
import toughcircle.shop.repository.DeliveryInfoRepository;

@Service
@RequiredArgsConstructor
public class DeliveryInfoService {

    private final DeliveryInfoRepository deliveryInfoRepository;

    public DeliveryInfo createDeliveryInfo(DeliveryInfoDto infoDto, Order order) {
        Address address = new Address();
        address.setZipCode(infoDto.getAddress().getZipCode());
        address.setStreetAddress(infoDto.getAddress().getStreetAddress());
        address.setAddressDetail(infoDto.getAddress().getAddressDetail());
        address.setAddressType(infoDto.getAddress().getAddressType());

        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setOrder(order);
        deliveryInfo.setOrderMemo(infoDto.getOrderMemo());
        deliveryInfo.setPhone(infoDto.getPhone());
        deliveryInfo.setAddress(address);
        deliveryInfo.setRecipientName(infoDto.getRecipientName());

        return deliveryInfo;
    }
}
