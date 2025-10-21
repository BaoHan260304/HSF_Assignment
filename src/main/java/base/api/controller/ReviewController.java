package base.api.controller;

import base.api.dto.request.ReviewRequest;
import base.api.dto.ReviewResponse;
import base.api.model.Review;
import base.api.security.CustomUserDetails;
import base.api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // Helper to convert Review entity to ReviewResponse DTO
    private ReviewResponse convertToDto(Review review) {
        ReviewResponse dto = new ReviewResponse();
        dto.setCustomerId(review.getCustomer().getId());
        dto.setCustomerName(review.getCustomer().getCustomerName());
        dto.setCarId(review.getCar().getId());
        dto.setCarName(review.getCar().getCarName());
        dto.setReviewStar(review.getReviewStar());
        dto.setComment(review.getComment());
        return dto;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')") // Only CUSTOMER can create reviews
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody ReviewRequest request) {
        try {
            Review newReview = reviewService.createReview(
                    currentUser.getId(),
                    request.getCarId(),
                    request.getReviewStar(),
                    request.getComment()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(newReview));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Review already exists
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Car or Customer not found
        }
    }

    @GetMapping("/car/{carId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')") // Both can view reviews for a car
    public ResponseEntity<List<ReviewResponse>> getReviewsByCar(@PathVariable Long carId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByCar(carId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/my-reviews")
    @PreAuthorize("hasRole('CUSTOMER')") // Customer can view their own reviews
    public ResponseEntity<List<ReviewResponse>> getMyReviews(@AuthenticationPrincipal CustomUserDetails currentUser) {
        List<ReviewResponse> reviews = reviewService.getReviewsByCustomer(currentUser.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{carId}")
    @PreAuthorize("hasRole('CUSTOMER')") // Customer can update their own review
    public ResponseEntity<ReviewResponse> updateReview(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long carId,
            @RequestBody ReviewRequest request) {
        try {
            Review updatedReview = reviewService.updateReview(
                    currentUser.getId(),
                    carId,
                    request.getReviewStar(),
                    request.getComment()
            );
            return ResponseEntity.ok(convertToDto(updatedReview));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{carId}")
    @PreAuthorize("hasRole('CUSTOMER')") // Customer can delete their own review
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long carId) {
        try {
            reviewService.deleteReview(currentUser.getId(), carId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}