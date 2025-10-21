package base.api.service.impl;

import base.api.model.Car;
import base.api.model.CarRental;
import base.api.model.Customer;
import base.api.repository.CarRentalRepository;
import base.api.repository.CarRepository;
import base.api.repository.CustomerRepository;
import base.api.service.CarRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarRentalServiceImpl implements CarRentalService {

    private final CarRentalRepository carRentalRepository;
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;

    @Override
    public CarRental createRental(Long customerId, Long carId, LocalDate pickupDate, LocalDate returnDate) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (pickupDate.isAfter(returnDate)) {
            throw new IllegalArgumentException("Pickup date must be before return date");
        }

        CarRental rental = new CarRental();
        rental.setCustomer(customer);
        rental.setCar(car);
        rental.setPickupDate(pickupDate);
        rental.setReturnDate(returnDate);
        rental.setRentPrice(car.getRentPrice() * (returnDate.toEpochDay() - pickupDate.toEpochDay()));
        rental.setStatus("Booked");

        return carRentalRepository.save(rental);
    }

    @Override
    public List<CarRental> getRentalHistoryForCustomer(Long customerId) {
        return carRentalRepository.findByCustomer_Id(customerId);
    }

    @Override
    public List<CarRental> getRentalReport(LocalDate startDate, LocalDate endDate) {
        return carRentalRepository.findByPickupDateBetweenOrderByPickupDateDesc(startDate, endDate);
    }
}