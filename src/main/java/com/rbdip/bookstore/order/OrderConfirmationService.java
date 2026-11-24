package com.rbdip.bookstore.order;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** Sends the confirmation produced by the order creation use case. */
@Service
public class OrderConfirmationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConfirmationService.class);

    public void send(String customerName, Long orderId, BigDecimal total) {
        // A real mail transport is outside the scope of the training project.
        LOGGER.info("[email] Dear {}, your order #{} for {} has been placed.", customerName, orderId, total);
    }
}
