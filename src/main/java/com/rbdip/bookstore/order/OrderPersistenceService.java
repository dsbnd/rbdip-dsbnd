package com.rbdip.bookstore.order;

import com.rbdip.bookstore.product.Product;
import java.util.List;
import org.springframework.stereotype.Service;

/** Persists an order and its resolved line items. */
@Service
public class OrderPersistenceService {

    private static final String NEW_ORDER_STATUS = "new";

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderPersistenceService(
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order save(CreateOrderRequest request, List<ResolvedOrderItem> items) {
        Customer customer = findOrCreateCustomer(request);
        Order order = new Order(customer, NEW_ORDER_STATUS);
        Order savedOrder = orderRepository.save(order);
        items.forEach(item -> saveItem(savedOrder, item));
        return savedOrder;
    }

    private void saveItem(Order order, ResolvedOrderItem item) {
        Product product = item.product();
        orderItemRepository.save(new OrderItem(order.getId(), product, item.quantity()));
    }

    private Customer findOrCreateCustomer(CreateOrderRequest request) {
        CustomerName customerName = CustomerName.from(request.customerFullName());
        return customerRepository
                .findFirstByFirstNameAndLastNameAndAddressAndPhone(
                        customerName.firstName(),
                        customerName.lastName(),
                        request.customerAddress(),
                        request.customerPhone())
                .orElseGet(() -> customerRepository.save(new Customer(
                        customerName.firstName(),
                        customerName.lastName(),
                        request.customerAddress(),
                        request.customerPhone())));
    }

    private record CustomerName(String firstName, String lastName) {

        private static CustomerName from(String fullName) {
            String[] parts = fullName.trim().split("\\s+", 2);
            return new CustomerName(parts[0], parts.length == 1 ? "" : parts[1]);
        }
    }
}
