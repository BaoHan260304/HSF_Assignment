package base.api.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CarRentalRequest {
    private Long carId;
    private LocalDate pickupDate;
    private LocalDate returnDate;
}