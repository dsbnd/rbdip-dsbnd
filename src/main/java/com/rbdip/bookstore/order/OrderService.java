package com.rbdip.bookstore.order;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Coordinates the order creation use case. Validation, product resolution,
 * persistence and customer notification are delegated to focused components.
 */
@Service
public class OrderService {

    private static final String REGULAR_CUSTOMER = "regular";

    private final OrderRequestValidator requestValidator;
    private final OrderItemResolver itemResolver;
    private final PricingCalculator pricingCalculator;
    private final OrderPersistenceService persistenceService;
    private final OrderConfirmationService confirmationService;

    public OrderService(
            OrderRequestValidator requestValidator,
            OrderItemResolver itemResolver,
            PricingCalculator pricingCalculator,
            OrderPersistenceService persistenceService,
            OrderConfirmationService confirmationService) {
        this.requestValidator = requestValidator;
        this.itemResolver = itemResolver;
        this.pricingCalculator = pricingCalculator;
        this.persistenceService = persistenceService;
        this.confirmationService = confirmationService;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        requestValidator.validate(request);
        List<ResolvedOrderItem> items = itemResolver.resolve(request.items());
        BigDecimal total = calculateTotal(request, items);
        Order order = persistenceService.save(request, items);
        confirmationService.send(request.customerFullName(), order.getId(), total);

        return order;
    }

    private BigDecimal calculateTotal(CreateOrderRequest request, List<ResolvedOrderItem> items) {
        List<PricingCalculator.LineItem> lineItems = items.stream()
                .map(item -> new PricingCalculator.LineItem(item.product().getPrice(), item.quantity()))
                .toList();
        String customerType = request.customerType() == null ? REGULAR_CUSTOMER : request.customerType();
        return pricingCalculator.calculateOrderTotal(lineItems, customerType, request.couponCode());
    }
}
