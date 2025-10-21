package base.api.service.impl;

import base.api.model.Car;
import base.api.model.Customer;
import base.api.model.Review;
import base.api.repository.CarRepository;
import base.api.repository.CustomerRepository;
import base.api.repository.ReviewRepository;
import base.api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;

    @Override
    @Transactional
    public Review createReview(Long customerId, Long carId, Integer reviewStar, String comment) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + carId));

        // Check if review already exists for this customer and car
        Review.ReviewId reviewId = new Review.ReviewId();
        reviewId.setCustomer(customerId);
        reviewId.setCar(carId);

        if (reviewRepository.findById(reviewId).isPresent()) {
            throw new IllegalStateException("Review already exists for this customer and car.");
        }

        Review review = new Review();
        review.setCustomer(customer);
        review.setCar(car);
        review.setReviewStar(reviewStar);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByCar(Long carId) {
        return reviewRepository.findByCar_Id(carId);
    }

    @Override
    public List<Review> getReviewsByCustomer(Long customerId) {
        return reviewRepository.findByCustomer_Id(customerId);
    }

    @Override
    public Optional<Review> getReviewById(Long customerId, Long carId) {
        Review.ReviewId reviewId = new Review.ReviewId();
        reviewId.setCustomer(customerId);
        reviewId.setCar(carId);
        return reviewRepository.findById(reviewId);
    }

    @Override
    @Transactional
    public Review updateReview(Long customerId, Long carId, Integer reviewStar, String comment) {
        Review.ReviewId reviewId = new Review.ReviewId();
        reviewId.setCustomer(customerId);
        reviewId.setCar(carId);

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found for customer " + customerId + " and car " + carId));

        existingReview.setReviewStar(reviewStar);
        existingReview.setComment(comment);

        return reviewRepository.save(existingReview);
    }

    @Override
    public void deleteReview(Long customerId, Long carId) {
        Review.ReviewId reviewId = new Review.ReviewId();
        reviewId.setCustomer(customerId);
        reviewId.setCar(carId);
        reviewRepository.deleteById(reviewId);
    }
}