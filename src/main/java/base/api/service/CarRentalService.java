package base.api.service;

import base.api.model.CarRental;

import java.time.LocalDate;
import java.util.List;

public interface CarRentalService {
    CarRental createRental(Long customerId, Long carId, LocalDate pickupDate, LocalDate returnDate);

    List<CarRental> getRentalHistoryForCustomer(Long customerId);

    List<CarRental> getRentalReport(LocalDate startDate, LocalDate endDate);
}