package base.api.controller;

import base.api.dto.request.CarRequest;
import base.api.dto.CarResponse;
import base.api.model.Car;
import base.api.model.CarProducer;
import base.api.repository.CarProducerRepository;
import base.api.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;
    private final CarProducerRepository carProducerRepository; // To fetch producer for CarRequest

    // Helper to convert Car entity to CarResponse DTO
    private CarResponse convertToDto(Car car) {
        CarResponse dto = new CarResponse();
        dto.setId(car.getId());
        dto.setCarName(car.getCarName());
        dto.setCarModelYear(car.getCarModelYear());
        dto.setColor(car.getColor());
        dto.setCapacity(car.getCapacity());
        dto.setDescription(car.getDescription());
        dto.setImportDate(car.getImportDate());
        dto.setRentPrice(car.getRentPrice());
        dto.setStatus(car.getStatus());
        if (car.getProducer() != null) {
            dto.setProducerName(car.getProducer().getProducerName());
        }
        return dto;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')") // Both roles can view cars
    public ResponseEntity<List<CarResponse>> getAllCars() {
        List<CarResponse> cars = carService.findAllCars().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return carService.findCarById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can add cars
    public ResponseEntity<CarResponse> createCar(@RequestBody CarRequest carRequest) {
        Car car = new Car();
        car.setCarName(carRequest.getCarName());
        car.setCarModelYear(carRequest.getCarModelYear());
        car.setColor(carRequest.getColor());
        car.setCapacity(carRequest.getCapacity());
        car.setDescription(carRequest.getDescription());
        car.setImportDate(carRequest.getImportDate());
        car.setRentPrice(carRequest.getRentPrice());
        car.setStatus(carRequest.getStatus());

        CarProducer producer = carProducerRepository.findById(carRequest.getProducerId())
                .orElseThrow(() -> new RuntimeException("CarProducer not found with id: " + carRequest.getProducerId()));
        car.setProducer(producer);

        Car savedCar = carService.saveCar(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedCar));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can update cars
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id, @RequestBody CarRequest carRequest) {
        return carService.findCarById(id).map(existingCar -> {
            existingCar.setCarName(carRequest.getCarName());
            existingCar.setCarModelYear(carRequest.getCarModelYear());
            existingCar.setColor(carRequest.getColor());
            existingCar.setCapacity(carRequest.getCapacity());
            existingCar.setDescription(carRequest.getDescription());
            existingCar.setImportDate(carRequest.getImportDate());
            existingCar.setRentPrice(carRequest.getRentPrice());
            existingCar.setStatus(carRequest.getStatus());

            if (carRequest.getProducerId() != null && !existingCar.getProducer().getId().equals(carRequest.getProducerId())) {
                CarProducer producer = carProducerRepository.findById(carRequest.getProducerId())
                        .orElseThrow(() -> new RuntimeException("CarProducer not found with id: " + carRequest.getProducerId()));
                existingCar.setProducer(producer);
            }

            Car updatedCar = carService.saveCar(existingCar);
            return ResponseEntity.ok(convertToDto(updatedCar));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only ADMIN can delete/change status of cars
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}