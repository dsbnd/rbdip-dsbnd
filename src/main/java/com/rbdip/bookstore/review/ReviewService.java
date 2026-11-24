package com.rbdip.bookstore.review;

import com.rbdip.bookstore.purchase.PurchaseVerification;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Review module uses only the public purchase contract and stays independent
 * from the order module implementation.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PurchaseVerification purchaseVerification;

    public ReviewService(
            ReviewRepository reviewRepository,
            PurchaseVerification purchaseVerification) {
        this.reviewRepository = reviewRepository;
        this.purchaseVerification = purchaseVerification;
    }

    public Review addReview(Long productId, String authorName, Integer rating, String comment) {
        purchaseVerification.hasAnyPurchase(productId);
        Review review = new Review(productId, authorName == null ? "anonymous" : authorName, rating, comment);
        return reviewRepository.save(review);
    }

    public List<Review> listReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
