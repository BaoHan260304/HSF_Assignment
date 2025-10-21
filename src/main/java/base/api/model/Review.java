package base.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
@IdClass(Review.ReviewId.class)
public class Review {

    @Id
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "review_star", nullable = false)
    private Integer reviewStar;

    @Column(nullable = false)
    private String comment;

    // Lớp khóa chính phức hợp
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewId implements Serializable {
        private Long customer;
        private Long car;
    }
}