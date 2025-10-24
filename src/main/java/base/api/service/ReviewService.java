package base.api.service;

import base.api.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review createReview(Long customerId, Long carId, Integer reviewStar, String comment);

    List<Review> getReviewsByCar(Long carId);

    List<Review> getReviewsByCustomer(Long customerId);

    Optional<Review> getReviewById(Long customerId, Long carId);

    Review updateReview(Long customerId, Long carId, Integer reviewStar, String comment);

    void deleteReview(Long customerId, Long carId);
}