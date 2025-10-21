package base.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car_rental")
@IdClass(CarRental.CarRentalId.class)
public class CarRental {

    @Id
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "pickup_date", nullable = false)
    private java.time.LocalDate pickupDate;

    @Column(name = "return_date", nullable = false)
    private java.time.LocalDate returnDate;

    @Column(name = "rent_price", nullable = false)
    private Double rentPrice;

    @Column(nullable = false)
    private String status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarRentalId implements java.io.Serializable {
        private Long customer;
        private Long car;
    }
}
