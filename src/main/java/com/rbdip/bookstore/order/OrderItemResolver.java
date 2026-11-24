package com.rbdip.bookstore.order;

import com.rbdip.bookstore.product.Product;
import com.rbdip.bookstore.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/** Resolves product references and quantities from an incoming order request. */
@Component
public class OrderItemResolver {

    private static final int DEFAULT_QUANTITY = 1;

    private final ProductRepository productRepository;

    public OrderItemResolver(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ResolvedOrderItem> resolve(List<CreateOrderRequest.Item> requestedItems) {
        return requestedItems.stream().map(this::resolveItem).toList();
    }

    private ResolvedOrderItem resolveItem(CreateOrderRequest.Item requestedItem) {
        Product product = productRepository.findById(requestedItem.productId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "product " + requestedItem.productId() + " not found"));
        int quantity = requestedItem.quantity() == null ? DEFAULT_QUANTITY : requestedItem.quantity();
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        return new ResolvedOrderItem(product, quantity);
    }
}
