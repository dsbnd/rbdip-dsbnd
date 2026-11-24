package com.rbdip.bookstore.order;

import com.rbdip.bookstore.purchase.PurchaseVerification;
import org.springframework.stereotype.Service;

/** Order-module adapter implementing the public purchase verification contract. */
@Service
public class OrderPurchaseVerification implements PurchaseVerification {

    private final OrderItemRepository orderItemRepository;

    public OrderPurchaseVerification(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public boolean hasAnyPurchase(Long productId) {
        return orderItemRepository.existsByProductId(productId);
    }
}
