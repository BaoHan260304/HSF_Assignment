package base.api.repository;

import base.api.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Review.ReviewId> {
    List<Review> findByCar_Id(Long carId);
    List<Review> findByCustomer_Id(Long customerId);

    @Query("SELECT r FROM Review r WHERE r.car.id = :carId AND r.customer.id = :customerId")
    Review findByCarIdAndCustomerId(@Param("carId") Long carId, @Param("customerId") Long customerId);
}