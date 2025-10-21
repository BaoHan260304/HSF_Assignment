package base.api.service.impl;

import base.api.model.Car;
import base.api.repository.CarRentalRepository;
import base.api.repository.CarRepository;
import base.api.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarRentalRepository carRentalRepository;

    @Override
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> findCarById(Long id) {
        return carRepository.findById(id);
    }

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public void deleteCar(Long id) {
        boolean isInTransaction = carRentalRepository.existsByCar_Id(id);
        if (isInTransaction) {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
            car.setStatus("Rented"); // Or "Archived", "Unavailable"
            carRepository.save(car);
        } else {
            carRepository.deleteById(id);
        }
    }
}