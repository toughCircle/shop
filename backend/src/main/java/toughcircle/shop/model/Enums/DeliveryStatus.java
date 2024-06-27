package toughcircle.shop.model.Enums;

public enum DeliveryStatus {
    NEW,              // 주문이 새로 생성된 상태
    PAID,             // 결제가 완료된 상태
    PROCESSING,       // 주문이 처리 중인 상태
    SHIPPED,          // 주문이 발송된 상태
    DELIVERED,        // 주문이 고객에게 전달된 상태
    CANCELLED,        // 주문이 취소된 상태
    RETURNED,         // 주문이 반품된 상태
    REFUNDED          // 주문이 환불된 상태
}
