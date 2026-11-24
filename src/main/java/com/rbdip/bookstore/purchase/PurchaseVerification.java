package com.rbdip.bookstore.purchase;

/** Public contract used by modules that need purchase information. */
public interface PurchaseVerification {

    boolean hasAnyPurchase(Long productId);
}
