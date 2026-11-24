package com.rbdip.bookstore.order;

import org.springframework.stereotype.Component;

/** Validates the order data required by the creation use case. */
@Component
public class OrderRequestValidator {

    public void validate(CreateOrderRequest request) {
        requireCustomerName(request);
        requireCustomerAddress(request);
        requireItems(request);
    }

    private void requireCustomerName(CreateOrderRequest request) {
        if (request.customerFullName() == null || request.customerFullName().isBlank()) {
            throw new IllegalArgumentException("customerFullName is required");
        }
    }

    private void requireCustomerAddress(CreateOrderRequest request) {
        if (request.customerAddress() == null || request.customerAddress().isBlank()) {
            throw new IllegalArgumentException("customerAddress is required");
        }
    }

    private void requireItems(CreateOrderRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("order must contain at least one item");
        }
    }
}
