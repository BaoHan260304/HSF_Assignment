package base.api.dto.response;

import base.api.enums.CarRentalStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CarRentalResponse {
    private Long customerId;
    private String customerName;
    private Long carId;
    private String carName;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private Double rentPrice;
    private CarRentalStatus status;
}