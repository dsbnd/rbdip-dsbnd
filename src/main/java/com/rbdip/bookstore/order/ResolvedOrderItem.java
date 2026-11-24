package com.rbdip.bookstore.order;

import com.rbdip.bookstore.product.Product;

/** A requested order item after its product and effective quantity are resolved. */
public record ResolvedOrderItem(Product product, int quantity) {
}
